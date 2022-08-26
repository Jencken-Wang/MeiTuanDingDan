package com.meituan.order.service.impl;

import cn.hutool.http.HttpRequest;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.meituan.order.service.IOrderDetailService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Optional;

@Slf4j
@Service
public class OrderDetailServiceImpl implements IOrderDetailService {

    /**
     * 获取单个订单详情信息
     * @param orderId
     * @param Cookie
     *接口获取数据格式见resources/orderDetail.json
     */
    @Override
    public JSONObject OrderItemList(String orderId, String Cookie) {
        log.info("orderId是：" + orderId);
        HashMap paramMap = new HashMap();
        paramMap.put("orderId", orderId);

        HashMap headerMap = new HashMap();
        headerMap.put("Accept", "application/json, text/plain, */*");
        headerMap.put("Accept-Language", "zh-CN,zh;q=0.9,en-US;q=0.8,en;q=0.7,zh-TW;q=0.6");
        headerMap.put("Cookie", Cookie);
        headerMap.put("Referer", "https://pos.meituan.com/web/fe.rms-portal/rms-report.html");
        headerMap.put("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/102.0.0.0 Safari/537.36");
        headerMap.put("appCode", "49");
        headerMap.put("model", "chrome");

        String result = HttpRequest.get("https://pos.meituan.com/web/api/v1/orders/detail")
                .headerMap(headerMap, true)
                .form(paramMap)
                .timeout(2000)
                .execute()
                .body();

//        log.info("orderDetail:\n" + JSONUtil.toJsonStr(result));

        //code不为0失败，结束
        if (Optional.ofNullable(JSONUtil.parseObj(result)).map(x -> x.getInt("code")).get() != 0) {
            log.error( orderId+"获取订单信息失败！！！");
            return null;
        }

        return Optional.ofNullable(JSONUtil.parseObj(result)).map(x->x.getJSONObject("data")).orElse(null);
    }



}
