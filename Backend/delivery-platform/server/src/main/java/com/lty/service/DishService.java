package com.lty.service;

import com.lty.dto.DishDTO;
import org.springframework.stereotype.Service;

/**
 * @author lty
 */
public interface DishService {
    public void saveWithFlavor(DishDTO dishDTO);
}
