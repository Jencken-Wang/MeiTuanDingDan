package com.meituan.order.service;

import cn.hutool.json.JSONObject;
import com.meituan.order.entity.dto.MenuIncomeDto;

public interface IOrderDetailService {
    JSONObject OrderItemList(String orderId, String Cookie);

}
