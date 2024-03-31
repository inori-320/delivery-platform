package com.lty.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.lty.dto.DishDTO;
import com.lty.dto.DishPageQueryDTO;
import com.lty.entity.Dish;
import com.lty.entity.DishFlavor;
import com.lty.mapper.DishMapper;
import com.lty.mapper.FlavorMapper;
import com.lty.result.PageResult;
import com.lty.service.DishService;
import com.lty.vo.DishVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author lty
 */
@Service
@Slf4j
public class DishServiceImpl implements DishService {
    @Autowired
    private DishMapper dishMapper;
    @Autowired
    private FlavorMapper flavorMapper;

    @Transactional
    public void saveWithFlavor(DishDTO dishDTO) {
        Dish dish = new Dish();
        BeanUtils.copyProperties(dishDTO, dish);
        dishMapper.insertNewDish(dish);
        Long id = dish.getId();
        List<DishFlavor> flavors = dishDTO.getFlavors();
        if(flavors != null && !flavors.isEmpty()){
            flavors.forEach(dishFlavor -> {
                dishFlavor.setDishId(id);
            });
            flavorMapper.insertBatch(flavors);
        }
    }

    public PageResult pageQuery(DishPageQueryDTO queryDTO) {
        PageHelper.startPage(queryDTO.getPage(), queryDTO.getPageSize());
        Page<DishVO> page = dishMapper.pageQuery(queryDTO);
        return new PageResult(page.getTotal(), page.getResult());
    }
}
