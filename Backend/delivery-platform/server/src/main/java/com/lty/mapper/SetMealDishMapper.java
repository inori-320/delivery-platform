package com.lty.mapper;

import com.lty.annotation.AutoFill;
import com.lty.entity.SetmealDish;
import com.lty.enumeration.OperationType;
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

    void insertBatch(List<SetmealDish> setmealDishes);
}
