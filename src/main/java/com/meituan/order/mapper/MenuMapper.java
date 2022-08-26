package com.meituan.order.mapper;

import com.meituan.order.entity.Menu;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * <p>
 * 菜单 Mapper 接口
 * </p>
 *
 * @author wzg
 * @since 2022-08-04
 */
@Mapper
public interface MenuMapper extends BaseMapper<Menu> {


    /**
     * 批量插入 仅适用于mysql
     * @param entityList 实体列表
     * @return 影响行数
     */
    Integer insertBatchSomeColumn(List<Menu> entityList);
}
