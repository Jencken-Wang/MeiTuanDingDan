package com.meituan.order.controller;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateField;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import com.meituan.order.entity.Menu;
import com.meituan.order.entity.dto.MenuIncomeDto;
import com.meituan.order.service.IMenuService;
import com.meituan.order.service.IOrderDetailService;
import com.meituan.order.service.IOrderService;
import com.meituan.order.util.Constant;
import com.meituan.order.util.MenuTypeEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import reactor.util.annotation.Nullable;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.util.*;
import java.util.stream.Collectors;

import static com.meituan.order.util.MenuTypeEnum.getEnumByCode;

@Slf4j
@Controller
@RequestMapping("/orderDetail")
public class OrderDetailController {

    @Resource
    private IOrderDetailService orderDetailService;

    @Resource
    private IMenuService menuService;

    @Resource
    private IOrderService orderService;

    @Value("${file.path}")
    private String path;

    Map<String, Integer> menuTypeMap;
    @PostConstruct
    public void setMenuTypeMap(){
        menuTypeMap = menuService.list().stream()
                .map(item -> {if(item.getDishesType() == null) item.setDishesType(5); return item;})
                .collect(Collectors.toMap(Menu::getDishesName, Menu::getDishesType));
    }

    /**
     * 分类统计菜品收入
     */
    @RequestMapping("/typeIncome")
    public List<MenuIncomeDto> typeIncome(@RequestParam String startTime, @RequestParam String endTime) {
        try {
            List<MenuIncomeDto> orderIncoms = new ArrayList<>();
            //orderList data数据
            JSONObject ordersJson = orderService.getOrderList(startTime,endTime, Constant.cookie);
            //orderList 具体数据
            JSONArray orderArray = Optional.ofNullable(ordersJson).map(x -> x.getJSONArray("orderList")).orElse(null);
            if (orderArray == null) {
                log.error("订单列表查询成功，但返回数据为空！");
                return null;
            }
            List<String> orderIds = JSONUtil.toList(orderArray, JSONObject.class).stream()
                    .map(item -> Optional.ofNullable(item).map(x -> x.getJSONObject("orderBase")).map(x -> x.getStr("id")).orElse(null))
                    .filter(item -> StrUtil.isNotBlank(item))
                    .collect(Collectors.toList());

            orderIds.forEach(id -> orderIncoms.add(acountMenuIncome(id)));

            log.info("统计订单收入结束\n" + JSONUtil.toJsonStr(orderIncoms));

            return orderIncoms;
        } catch (Exception e) {
            log.info(e.getMessage());
            e.printStackTrace();
            return null;
        }
    }


    /**
     * 收入文件生成
     * @param startTime
     * @param endTime
     * @param fileName
     */
    @RequestMapping("/typeIncome/downloadFile")
    public void TypeIncomeFile(@RequestParam String startTime, @RequestParam String endTime, @RequestParam(required = false) String fileName, @Nullable HttpServletResponse response) {
        try {
            log.info("开始生成菜品收入文件");
            List<MenuIncomeDto> incomeDtos = new ArrayList<>();
            //时间超过10天分段查询
            Date start = DateUtil.parse(startTime);
            Date end = DateUtil.parse(endTime);
            Date startIndex = start;
            Date endIndex = DateUtil.offsetDay(start, 10).setField(DateField.HOUR_OF_DAY, 23).setField(DateField.MINUTE, 59).setField(DateField.SECOND, 59);
            while (true) {
                if (DateUtil.compare(endIndex, end) < 0) {
                    log.info(startIndex + " " + endIndex);
                    incomeDtos.addAll(typeIncome(DateUtil.formatDateTime(startIndex), DateUtil.formatDateTime(endIndex)));
                    startIndex = DateUtil.offsetSecond(endIndex, 1);
                    endIndex = DateUtil.offsetDay(endIndex, 10);
                } else{
                    log.info(startIndex + " " + end);
                    incomeDtos.addAll(typeIncome(DateUtil.formatDateTime(startIndex), DateUtil.formatDateTime(end)));
                    break;
                }
            }

            if (StrUtil.isBlank(fileName)) {
                fileName = DateUtil.format(start, "yyyyMMddHHmmss") + "-" + DateUtil.format(end, "yyyyMMddHHmmss") + "income.xlsx";
            }else if (!fileName.endsWith(".xlsx")) {
                fileName = fileName + ".xlsx";
            }
            File excelFile = new File(System.getProperty("user.dir") + path + fileName);
            ExcelWriter writer = ExcelUtil.getWriter(excelFile);
            writer.addHeaderAlias("orderId", "id");
            writer.addHeaderAlias("orderNum", "订单号");
            writer.addHeaderAlias("foodIncome", "食品收入");
            writer.addHeaderAlias("alcoholIncome", "酒水收入");
            writer.addHeaderAlias("drinksIncome", "饮料收入");
            writer.addHeaderAlias("otherIncome", "其他收入");
            writer.addHeaderAlias("uncategorizedIncome", "未分类收入");
            writer.addHeaderAlias("foodList", "食品列表");
            writer.addHeaderAlias("alcoholList", "酒水列表");
            writer.addHeaderAlias("drinksList", "饮料列表");
            writer.addHeaderAlias("otherList", "其他列表");
            writer.addHeaderAlias("uncategorizedList", "未分类列表");
            writer.addHeaderAlias("comment", "备注");

            ArrayList<MenuIncomeDto> menuIncomeRows = CollUtil.newArrayList(incomeDtos);
            writer.write(menuIncomeRows, true);
            if (response != null) {
                ServletOutputStream outputStream = response.getOutputStream();
                response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(fileName, "UTF-8"));
                response.setContentType("application/octet-stream");
//                response.setContentType("application/vnd.ms-excel;charset=utf-8");
                writer.flush(outputStream, true);
            }
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
            log.info("收入文件下载出错！"+e.getMessage());
        }
        log.info("生成菜品收入文件结束");
    }



    /**
     * 根据订单号 分类统计菜品收入
     * @param orderId
     * @return
     */
    @RequestMapping("/acountMenuIncome")
    public MenuIncomeDto acountMenuIncome(@RequestParam String orderId) {
        MenuIncomeDto incomeDto = new MenuIncomeDto();
        try {
            //获取订单详情
            JSONObject detailJson = orderDetailService.OrderItemList(orderId, Constant.cookie);
            Optional<JSONObject> detailJsonOp = Optional.ofNullable(detailJson);
            if(!detailJsonOp.isPresent()){
                log.info("订单id：{}，获取详情失败", orderId);
                return null;
            }
            incomeDto.setOrderId(orderId);
            incomeDto.setOrderNum(detailJsonOp.map(x -> x.getJSONObject("orderBase")).map(x -> x.getStr("orderNo")).orElse(""));

            //菜品列表
            JSONArray jsonArray = detailJsonOp.map(x -> x.getJSONArray("itemList")).orElse(new JSONArray());


            /**
             * 菜品中有树状关系，一个套餐包含多个菜品，菜品会和套餐一同列出来，因此统计时直接将套餐收入计入总收入，排除套餐下菜品收入，避免重复计算.避免方式，itemNo和parentItemNo相同则是需要统计的
             */


            //分类统计收入
            JSONUtil.toList(jsonArray, JSONObject.class).stream().forEach(item -> {
                //菜品收入
                BigDecimal income = BigDecimal.valueOf(item.getInt("income")).divide(new BigDecimal(100));
                //备注
                String comment = item.getStr("comment");
                //每一项有属性name和supname，如果匹配不到类型，检测两个name是否一致
                String menuName = StrUtil.isNotBlank(item.getStr("spuName")) ? item.getStr("spuName") : item.getStr("name");

                comment = StrUtil.isBlank(comment) ? "" : " -- " + comment;


                String itemNo = item.getStr("itemNo");
                String parentItemNo = item.getStr("parentItemNo");
                if (!Objects.equals(itemNo, parentItemNo)) {
                    return;
                }
                MenuTypeEnum enumByCode = Optional.ofNullable(getEnumByCode(menuTypeMap.get(menuName))).orElse(MenuTypeEnum.uncategorized);
                switch (enumByCode){
                    case food:
                        incomeDto.setFoodIncome(incomeDto.getFoodIncome().add(income));
                        incomeDto.getFoodList().add(menuName + comment);
                        break;
                    case alcohol:
                        incomeDto.setAlcoholIncome(incomeDto.getAlcoholIncome().add(income));
                        incomeDto.getAlcoholList().add(menuName + comment);
                        break;
                    case drinks:
                        incomeDto.setDrinksIncome(incomeDto.getDrinksIncome().add(income));
                        incomeDto.getDrinksList().add(menuName + comment);
                        break;
                    case other:
                        incomeDto.setOtherIncome(incomeDto.getOtherIncome().add(income));
                        incomeDto.getOtherList().add(menuName + comment);
                        break;
                    default:
                        incomeDto.setUncategorizedIncome(incomeDto.getUncategorizedIncome().add(income));
                        incomeDto.getUncategorizedList().add(menuName + comment);
                        break;
                }
            });
            return incomeDto;
        } catch (Exception e) {
            log.info(e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

}
