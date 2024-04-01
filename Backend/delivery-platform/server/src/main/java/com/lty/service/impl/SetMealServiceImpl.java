package com.lty.service.impl;

import com.lty.dto.SetmealDTO;
import com.lty.entity.Dish;
import com.lty.entity.Setmeal;
import com.lty.entity.SetmealDish;
import com.lty.mapper.SetMealDishMapper;
import com.lty.mapper.SetMealMapper;
import com.lty.service.SetMealService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author lty
 */
@Service
public class SetMealServiceImpl implements SetMealService {
    @Autowired
    private SetMealMapper setMealMapper;
    @Autowired
    private SetMealDishMapper setMealDishMapper;

    @Transactional
    public void saveWithDish(SetmealDTO setmealDTO) {
        Setmeal setmeal = new Setmeal();
        BeanUtils.copyProperties(setmealDTO, setmeal);
        setMealMapper.insert(setmeal);
        Long setMealId = setmeal.getId();
        List<SetmealDish> setmealDishes = setmealDTO.getSetmealDishes();
        for (SetmealDish setmealDish: setmealDishes) {
            setmealDish.setSetmealId(setMealId);
        }
        setMealDishMapper.insertBatch(setmealDishes);
    }
}
