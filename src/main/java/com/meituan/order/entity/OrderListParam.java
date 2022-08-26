package com.meituan.order.entity;

import lombok.Data;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
public class OrderListParam {
    String queryTimeType;//1:下单时间，2：结账时间，4：营业时间
    Long startDate;//13位时间戳
    Long endDate;//13位时间戳
    List<String> typeList = new ArrayList<>();//100堂食，200整单外带，400部分外带
    List<Integer> unionTypeList = new ArrayList<>();//订单类型，0：普通非联合订单，1：联合订单
    Integer pageNo;//第几页
    Integer pageSize;//每页行数
    String login;//应该是登录id：a17774978353
    String loginName;//登录名：悦庭苑·重庆君豪
    Long beginTime;//13位时间戳
    Long endTime;//13位时间戳
    List<String> statusList = new ArrayList<>();//100：待接单、待制作， 200：未结账， 300：已结账， 600：已撤单， 500：已退单
    Integer sortField;
    Integer sort;
    Integer amountType;//不知道是什么，固定1


}
