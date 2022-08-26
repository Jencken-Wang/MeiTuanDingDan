package com.meituan.order.entity.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class MenuIncomeDto {


    //订单id
    private String orderId;
    //顶单号
    private String orderNum;
    //食品收入
    private BigDecimal foodIncome;
    //酒水收入
    private BigDecimal alcoholIncome;
    //饮料收入
    private BigDecimal drinksIncome;
    //其他收入
    private BigDecimal otherIncome;

    //未分类收入
    private BigDecimal uncategorizedIncome;

    //食品菜名
    private List<String> foodList;
    //酒水菜名
    private List<String> alcoholList;
    //饮料菜名
    private List<String> drinksList;

    private List<String> otherList;

    private List<String> uncategorizedList;


    //备注
    private String comment;

    public MenuIncomeDto() {
        this.foodList = new ArrayList<>();
        this.alcoholList = new ArrayList<>();
        this.drinksList = new ArrayList<>();
        this.otherList = new ArrayList<>();
        this.uncategorizedList = new ArrayList<>();

        this.foodIncome = new BigDecimal(0);
        this.alcoholIncome = new BigDecimal(0);
        this.drinksIncome = new BigDecimal(0);
        this.otherIncome = new BigDecimal(0);
        this.uncategorizedIncome = new BigDecimal(0);


    }
}
