package com.lty.service;

import com.lty.dto.SetmealDTO;
import com.lty.dto.SetmealPageQueryDTO;
import com.lty.entity.Dish;
import com.lty.result.PageResult;

import java.util.List;

/**
 * @author lty
 */
public interface SetMealService {

    void saveWithDish(SetmealDTO setmealDTO);

    PageResult pageQuery(SetmealPageQueryDTO queryDTO);

    void deleteOneBatch(List<Long> ids);
}
