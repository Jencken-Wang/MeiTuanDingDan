package com.meituan.order.service;

import cn.hutool.json.JSONObject;

public interface IOrderService {
    JSONObject getOrderList(String startTime, String endTime, String Cookie);
}
