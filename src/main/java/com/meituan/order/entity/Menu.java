package com.meituan.order.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 菜单
 * </p>
 *
 * @author wzg
 * @since 2022-08-04
 */
@Getter
@Setter
public class Menu implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 菜单名
     */
    private String dishesName;

    /**
     * 菜品类型：1：食品，2：酒水，3：饮料，4：其他
     */
    private Integer dishesType;

    /**
     * 菜品标签:1：类型不确定
     */
    private Integer dishesTag;

    /**
     * 备注
     */
    private String conment;


}
