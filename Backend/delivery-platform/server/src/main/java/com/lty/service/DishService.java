package com.lty.service;

import com.lty.dto.DishDTO;
import com.lty.dto.DishPageQueryDTO;
import com.lty.result.PageResult;
import org.springframework.stereotype.Service;

/**
 * @author lty
 */
public interface DishService {
    public void saveWithFlavor(DishDTO dishDTO);

    PageResult pageQuery(DishPageQueryDTO queryDTO);
}
