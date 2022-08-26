package com.meituan.order.controller;


import com.meituan.order.entity.Menu;
import com.meituan.order.service.IMenuService;
import com.meituan.order.util.Constant;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

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
    @RequestMapping("/syncMenus")
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
            menu.setDishesTag(1);
            menu.setConment("未分类");
            return menu;
        }).collect(Collectors.toList());

        if (allMenu.size() != 0) {
            menuService.saveBatch(newMenus);
        }
        log.info("同步到的菜品名为：" + allMenu);
    }
}