package com.meituan.order.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Slf4j
@Controller
@RequestMapping("/order")
public class OrderController {


    public void print() {
        System.out.println("123123123");
    }
}
