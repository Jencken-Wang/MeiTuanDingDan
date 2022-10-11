package com.meituan.order.service;

import com.meituan.order.entity.Menu;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 菜单 服务类
 * </p>
 *
 * @author wzg
 * @since 2022-08-04
 */
public interface IMenuService extends IService<Menu> {

    List<String> getAllMenu(String Cookie);

    List<String> getAllMenu(String Cookie, Long startTime, Long endTime);
}
