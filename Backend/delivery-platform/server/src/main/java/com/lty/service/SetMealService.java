package com.lty.service;

import com.lty.dto.SetmealDTO;
import com.lty.entity.Dish;

import java.util.List;

/**
 * @author lty
 */
public interface SetMealService {

    void saveWithDish(SetmealDTO setmealDTO);
}
