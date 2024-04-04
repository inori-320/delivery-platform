package com.lty.service;

import com.lty.dto.DishDTO;
import com.lty.dto.DishPageQueryDTO;
import com.lty.entity.Dish;
import com.lty.result.PageResult;
import com.lty.vo.DishVO;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author lty
 */
public interface DishService {
    public void saveWithFlavor(DishDTO dishDTO);

    PageResult pageQuery(DishPageQueryDTO queryDTO);

    void deleteOneOrBatch(List<Long> ids);

    DishVO getByIdWithFlavor(Long id);

    void updateDish(DishDTO dishDTO);

    List<Dish> selectDish(Long categoryId);

    List<DishVO> listWithFlavor(Dish dish);
}
