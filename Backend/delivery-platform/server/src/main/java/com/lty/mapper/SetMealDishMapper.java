package com.lty.mapper;

import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author lty
 */
@Mapper
public interface SetMealDishMapper {
    /**
     * 根据菜品ID查询套餐ID
     * @param dishIds
     * @return
     */
    List<Long> getSetMealIdsByDish(List<Long> dishIds);
}
