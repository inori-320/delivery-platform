package com.lty.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.lty.constant.MessageConstant;
import com.lty.constant.StatusConstant;
import com.lty.dto.DishDTO;
import com.lty.dto.DishPageQueryDTO;
import com.lty.entity.Dish;
import com.lty.entity.DishFlavor;
import com.lty.exception.DeletionNotAllowedException;
import com.lty.mapper.DishMapper;
import com.lty.mapper.FlavorMapper;
import com.lty.mapper.SetMealDishMapper;
import com.lty.mapper.SetMealMapper;
import com.lty.result.PageResult;
import com.lty.service.DishService;
import com.lty.vo.DishVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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
    @Autowired
    private SetMealDishMapper setMealDishMapper;
    @Autowired
    private SetMealMapper setMealMapper;

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

    /**
     * 单个或批量删除菜品
     * @param ids id列表
     */
    // TODO: 目前这个逻辑有问题，如果批量删除内有一个不行就全不行了，而且用到了多次循环，并且调用了多次数据库，需要优化
    @Transactional
    public void deleteOneOrBatch(List<Long> ids) {
        if(ids.isEmpty()) return;
        for (Long id : ids) {
            Dish dish = dishMapper.getById(id);
            if(Objects.equals(dish.getStatus(), StatusConstant.ENABLE)){
                throw new DeletionNotAllowedException(MessageConstant.DISH_ON_SALE);
            }
        }
        List<Long> setMealIds = setMealDishMapper.getSetMealIdsByDish(ids);
        if(setMealIds != null && !setMealIds.isEmpty()){
            throw new DeletionNotAllowedException(MessageConstant.DISH_BE_RELATED_BY_SETMEAL);
        }
        // 根据菜品ID集合批量删除菜品数据和关联的口味数据
        dishMapper.deleteByIds(ids);
        flavorMapper.deleteByDishIds(ids);
    }

    public DishVO getByIdWithFlavor(Long id) {
        Dish dish = dishMapper.getById(id);
        List<DishFlavor> dishFlavors = flavorMapper.getByDishId(id);
        DishVO dishVO = new DishVO();
        BeanUtils.copyProperties(dish, dishVO);
        dishVO.setFlavors(dishFlavors);
        return dishVO;
    }

    @Transactional
    public void updateDish(DishDTO dishDTO) {
        Dish dish = new Dish();
        BeanUtils.copyProperties(dishDTO, dish);
        // 修改菜品表基本信息
        dishMapper.update(dish);
        // 删除全部口味数据
        flavorMapper.deleteByDishId(dish.getId());
        // 修改成当前设置的口味数据
        List<DishFlavor> flavors = dishDTO.getFlavors();
        if(flavors != null && !flavors.isEmpty()){
            flavors.forEach(dishFlavor -> {
                dishFlavor.setDishId(dishDTO.getId());
            });
            flavorMapper.insertBatch(flavors);
        }
    }

    public List<Dish> selectDish(Long categoryId) {
        Dish dish = Dish.builder().categoryId(categoryId).status(StatusConstant.ENABLE).build();
        return dishMapper.selectDish(dish);
    }

    /**
     * 条件查询菜品和口味
     * @param dish
     * @return
     */
    public List<DishVO> listWithFlavor(Dish dish) {
        List<Dish> dishList = dishMapper.selectDish(dish);

        List<DishVO> dishVOList = new ArrayList<>();

        for (Dish d : dishList) {
            DishVO dishVO = new DishVO();
            BeanUtils.copyProperties(d,dishVO);

            //根据菜品id查询对应的口味
            List<DishFlavor> flavors = flavorMapper.getByDishId(d.getId());

            dishVO.setFlavors(flavors);
            dishVOList.add(dishVO);
        }

        return dishVOList;
    }
}
