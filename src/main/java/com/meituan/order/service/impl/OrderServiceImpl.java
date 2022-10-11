package com.meituan.order.service.impl;

import cn.hutool.core.date.DateUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.meituan.order.entity.OrderListParam;
import com.meituan.order.service.IOrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Optional;

@Service
@Slf4j
public class OrderServiceImpl implements IOrderService {

    /**
     * 获取时间范围内订单列表
     * @param startTime
     * @param endTime
     * @param Cookie
     * @return
     * 接口获取数据格式见resources/orderList.json
     */
    @Override
    public JSONObject getOrderList(String startTime, String endTime, String Cookie) {
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

            OrderListParam param = new OrderListParam();
            param.setAmountType(1);
            param.setQueryTimeType("4");
            param.setLogin("a17774978353");
            param.setLoginName("悦庭苑·重庆君豪");
            param.setBeginTime(DateUtil.parseDateTime(startTime).getTime());
            param.setEndTime(DateUtil.parseDateTime(endTime).getTime());
            param.setStartDate(DateUtil.parseDateTime(startTime).getTime());
            param.setEndDate(DateUtil.parseDateTime(endTime).getTime());
            param.setPageNo(1);
            param.setPageSize(1500);
            param.setSort(1);
            param.setSortField(3);
            param.getStatusList().add("300");
            param.getTypeList().addAll(Arrays.asList(new String[]{"100", "200", "400"}));
            param.getUnionTypeList().addAll(Arrays.asList(new Integer[]{0, 1}));

//        System.out.println(JSONUtil.toJsonStr(param).getBytes().length);

            String result = HttpRequest.post("https://pos.meituan.com/web/api/v2/reports/order-detail-instore/list")
                    .headerMap(headerMap, true)
                    .contentLength(JSONUtil.toJsonStr(param).getBytes().length)
                    .body(JSONUtil.toJsonStr(param))
                    .timeout(10000)
                    .execute()
                    .body();
            Optional<JSONObject> resultOp = Optional.ofNullable(JSONUtil.parseObj(result));
            if (resultOp.map(x -> x.getInt("code")).get() != 0) {
                log.error("获取{}到{}订单列表失败", startTime, endTime);
                return null;
            }
            return resultOp.map(x -> x.getJSONObject("data")).orElse(null);

        } catch (Exception e) {
            log.error(e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

}
