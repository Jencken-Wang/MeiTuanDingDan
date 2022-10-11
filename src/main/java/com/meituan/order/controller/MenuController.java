package com.meituan.order.controller;


import cn.hutool.core.date.DateField;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.meituan.order.entity.Menu;
import com.meituan.order.service.IMenuService;
import com.meituan.order.util.Constant;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * <p>
 * 菜单 前端控制器
 * </p>
 *
 * @author wzg
 * @since 2022-08-04
 */
@Slf4j
@Controller
@RequestMapping("/menu")
public class MenuController {


    @Resource
    IMenuService menuService;


    /**
     * 同步菜品名
     *
     * @return
     */
    @RequestMapping(value = "/syncMenus", method = RequestMethod.GET)
    public void syncMenus() {
        List<String> menus = new ArrayList<>();
        Optional.ofNullable(menuService.list()).ifPresent(x -> {
            x.forEach(item -> menus.add(item.getDishesName()));
        });

        List<String> allMenu = menuService.getAllMenu(Constant.cookie);

        allMenu.removeAll(menus);

        List<Menu> newMenus = allMenu.stream().map(el -> {
            Menu menu = new Menu();
            menu.setDishesName(el);
            menu.setDishesType(5);
            menu.setDishesTag(2);
            menu.setConment("未分类");
            return menu;
        }).collect(Collectors.toList());

        if (allMenu.size() != 0) {
            menuService.saveBatch(newMenus);
        }
        log.info("同步到的菜品名为：" + allMenu);
    }


    /**
     * 同步菜品名
     *
     * @return
     */
    @RequestMapping(value = "/syncHisMenus", method = RequestMethod.GET)
    public void syncMenus(@RequestParam(required = false) String startTime, @RequestParam(required = false) String endTime) {
        List<String> localMenus = new ArrayList<>();
        long start;
        long end;
        //未输入时间默认查询当月菜品
        if (StrUtil.isBlank(startTime) || StrUtil.isBlank(endTime)) {
            DateTime begin = DateTime.now();
            begin.setField(DateField.DAY_OF_MONTH, 1);
            begin.setField(DateField.HOUR, 0);
            begin.setField(DateField.MINUTE, 0);
            begin.setField(DateField.SECOND, 0);
            start = begin.getTime();
            end = DateUtil.date().getTime();
        } else {
            start = DateUtil.parseDateTime(startTime).getTime();
            end = DateUtil.parseDateTime(endTime).getTime();
        }


        //远端菜单
        List<String> allMenu = menuService.getAllMenu(Constant.cookie,start, end);
        //本地菜单
        Optional.ofNullable(menuService.list()).ifPresent(x -> {
            x.forEach(item -> localMenus.add(item.getDishesName()));
        });

        allMenu.removeAll(localMenus);

        List<Menu> newMenus = allMenu.stream().map(el -> {
            Menu menu = new Menu();
            menu.setDishesName(el);
            menu.setDishesType(5);
            menu.setDishesTag(2);
            menu.setConment("未分类");
            return menu;
        }).collect(Collectors.toList());

        if (allMenu.size() != 0) {
            menuService.saveBatch(newMenus);
        }
        log.info("同步到的菜品名为：" + allMenu);
    }
}