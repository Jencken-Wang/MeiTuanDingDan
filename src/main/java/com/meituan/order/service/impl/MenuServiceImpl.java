package com.meituan.order.service.impl;

import cn.hutool.http.HttpRequest;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.meituan.order.entity.Menu;
import com.meituan.order.mapper.MenuMapper;
import com.meituan.order.service.IMenuService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * <p>
 * 菜单 服务实现类
 * </p>
 *
 * @author wzg
 * @since 2022-08-04
 */
@Service
public class MenuServiceImpl extends ServiceImpl<MenuMapper, Menu> implements IMenuService {


    @Resource
    private MenuMapper menuMapper;


    /**
     * 获取远端上所有彩品名称
     * @param Cookie
     * @return
     */
    @Override
    public List<String> getAllMenu(String Cookie){
        List<String> menuNames = null;
        try {

            HashMap headerMap = new HashMap();
            headerMap.put("Accept", "application/json, text/plain, */*");
            headerMap.put("Accept-Language", "zh-CN,zh;q=0.9,en-US;q=0.8,en;q=0.7,zh-TW;q=0.6");
            headerMap.put("Content-Type", "application/json;charset=UTF-8");
            headerMap.put("Cookie", Cookie);
            headerMap.put("Referer", "https://pos.meituan.com/web/report/orderList?_fe_report_use_storage_query=true");
            headerMap.put("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/102.0.0.0 Safari/537.36");
            headerMap.put("appCode", "49");
            headerMap.put("model", "chrome");

            HashMap paramMap = new HashMap();
            paramMap.put("pageNo", 1);
            paramMap.put("pageSize", 1500);
            List list = new ArrayList<>();
            list.add(600396852);
            paramMap.put("poiIds", list);
            paramMap.put("isCognateDish", false);
            paramMap.put("showBanquet", 1);
            paramMap.put("goodsType", 10);

            JSONObject paramObj = new JSONObject();
            paramObj.set("reqJson", JSONUtil.toJsonStr(paramMap));

            String result = HttpRequest.post("https://pos.meituan.com/web/api/v2/reports/dict/search-deleted-dish-list")
                    .headerMap(headerMap, true)
                    .contentLength(JSONUtil.toJsonStr(paramObj).getBytes().length)
                    .body(JSONUtil.toJsonStr(paramObj))
                    .timeout(2000)
                    .execute()
                    .body();


            JSONObject resultObj = JSONUtil.parseObj(result);//返回结果json对象
            if (Optional.ofNullable(resultObj).map(x->x.getInt("code")==0).orElse(false)){
                menuNames = Optional.ofNullable(resultObj)
                        .map(x -> x.getJSONObject("data"))
                        .map(y -> y.getJSONArray("items"))
                        .orElse(new JSONArray())
                        .stream().map(item -> JSONUtil.parseObj(item).getStr("spuName"))
                        .collect(Collectors.toList());
//                System.out.println(JSONUtil.toJsonStr(Optional.ofNullable(resultObj)
//                        .map(x -> x.getJSONObject("data"))
//                        .map(y -> y.getJSONArray("items"))
//                        .orElse(new JSONArray())));

//                JSONObject dataObj = JSONUtil.parseObj(result).getJSONObject("data");
//                JSONArray menuArrayJson = dataObj.getJSONArray("items");
//                menuNames = menuArrayJson.stream().map(item -> JSONUtil.parseObj(item).getStr("spuName"))
//                        .collect(Collectors.toList());

            } else {
                log.error("菜单接口获取失败！");
            }

            for (String menuName : menuNames) {
                System.out.println(menuName);
            }
        } catch (Exception e) {
            log.error(e.getMessage());
            e.printStackTrace();
        }
        return menuNames;
    }

}
