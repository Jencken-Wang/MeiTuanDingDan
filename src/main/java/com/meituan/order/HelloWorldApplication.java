package com.meituan.order;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.util.ResourceUtils;

import java.io.FileNotFoundException;

@SpringBootApplication
public class HelloWorldApplication {

    public static void main(String[] args) throws FileNotFoundException {
        SpringApplication.run(HelloWorldApplication.class, args);
        System.out.println("打印项目路径");
        System.out.println(System.getProperty("user.dir"));
        System.out.println(ResourceUtils.getURL("classpath:").getPath());

    }

//
//    public static List<String> getOrderIds(JSONObject orders) {
//        JSONObject data = orders.getJSONObject("data");
//        JSONArray orderList = data.getJSONArray("orderList");
//        List<String> orderIds = new ArrayList<>();
//        List<String> orderNos = new ArrayList<>();
//        orderList.forEach(item -> {
//            JSONObject orderBase = JSONUtil.parseObj(item).getJSONObject("orderBase");
//            orderIds.add(orderBase.get("id").toString());
//            orderNos.add(orderBase.get("orderNo").toString());
//        });
//
//        for (String orderNo : orderNos) {
//            System.out.println(orderNo);
//        }
//        return orderIds;
//    }

}
