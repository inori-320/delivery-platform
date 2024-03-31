package com.lty.service;

import com.lty.dto.DishDTO;
import com.lty.dto.DishPageQueryDTO;
import com.lty.result.PageResult;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author lty
 */
public interface DishService {
    public void saveWithFlavor(DishDTO dishDTO);

    PageResult pageQuery(DishPageQueryDTO queryDTO);

    void deleteOneOrBatch(List<Long> ids);
}
