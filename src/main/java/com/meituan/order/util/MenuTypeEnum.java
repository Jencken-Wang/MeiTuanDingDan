package com.meituan.order.util;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

/**
 * 菜品类型枚举
 */
@Getter
public enum MenuTypeEnum {

    food("食品", 1),
    alcohol("酒水", 2),
    drinks("饮料", 3),
    other("其他", 4),
    uncategorized("未分类", 5);

    private String type;
    private int code;

    MenuTypeEnum(String type, int code) {
        this.type = type;
        this.code = code;
    }


    public static MenuTypeEnum getEnumByCode(Integer code) {
        if (code == null) {
            return null;
        }
        for (MenuTypeEnum e : MenuTypeEnum.values()) {
            if (e.getCode() == code) {
                return e;
            }
        }
        return null;
    }
}
