package com.lty.service;

import com.lty.dto.SetmealDTO;
import com.lty.dto.SetmealPageQueryDTO;
import com.lty.entity.Setmeal;
import com.lty.result.PageResult;
import com.lty.vo.DishItemVO;
import com.lty.vo.SetmealVO;

import java.util.List;

/**
 * @author lty
 */
public interface SetMealService {

    void saveWithDish(SetmealDTO setmealDTO);

    PageResult pageQuery(SetmealPageQueryDTO queryDTO);

    void deleteOneBatch(List<Long> ids);

    SetmealVO selectSetMealById(Long id);

    void updateSetMeal(SetmealDTO setmealDTO);

    void updateStatus(Integer status, Long id);

    /**
     * 条件查询
     * @param setmeal
     * @return
     */
    List<Setmeal> list(Setmeal setmeal);

    List<DishItemVO> getDishItemById(Long id);
}
