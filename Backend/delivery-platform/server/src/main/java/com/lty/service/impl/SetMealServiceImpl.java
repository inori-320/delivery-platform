package com.lty.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.lty.constant.MessageConstant;
import com.lty.constant.StatusConstant;
import com.lty.dto.SetmealDTO;
import com.lty.dto.SetmealPageQueryDTO;
import com.lty.entity.Dish;
import com.lty.entity.Setmeal;
import com.lty.entity.SetmealDish;
import com.lty.exception.DeletionNotAllowedException;
import com.lty.exception.SetmealEnableFailedException;
import com.lty.mapper.DishMapper;
import com.lty.mapper.SetMealDishMapper;
import com.lty.mapper.SetMealMapper;
import com.lty.result.PageResult;
import com.lty.service.SetMealService;
import com.lty.vo.DishItemVO;
import com.lty.vo.SetmealVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

/**
 * @author lty
 */
@Service
public class SetMealServiceImpl implements SetMealService {
    @Autowired
    private SetMealMapper setMealMapper;
    @Autowired
    private SetMealDishMapper setMealDishMapper;
    @Autowired
    private DishMapper dishMapper;

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

    public PageResult pageQuery(SetmealPageQueryDTO queryDTO) {
        PageHelper.startPage(queryDTO.getPage(), queryDTO.getPageSize());
        Page<SetmealVO> page = setMealMapper.pageQuery(queryDTO);
        return new PageResult(page.getTotal(), page.getResult());
    }

    @Transactional
    public void deleteOneBatch(List<Long> ids) {
        ids.forEach(id -> {
            Setmeal setmeal = setMealMapper.getById(id);
            if(Objects.equals(setmeal.getStatus(), StatusConstant.ENABLE)){
                throw new DeletionNotAllowedException(MessageConstant.SETMEAL_ON_SALE);
            }
        });
        ids.forEach(id -> {
            setMealMapper.deleteById(id);
            setMealDishMapper.deleteBySetMealId(id);
        });
    }

    public SetmealVO selectSetMealById(Long id) {
        Setmeal setmeal =  setMealMapper.getById(id);
        List<SetmealDish> setmealDishes = setMealDishMapper.getBySetMealId(id);
        SetmealVO vo = new SetmealVO();
        BeanUtils.copyProperties(setmeal, vo);
        vo.setSetmealDishes(setmealDishes);
        return vo;
    }

    @Transactional
    public void updateSetMeal(SetmealDTO setmealDTO) {
        Setmeal setmeal = new Setmeal();
        BeanUtils.copyProperties(setmealDTO, setmeal);
        setMealMapper.update(setmeal);
        Long id = setmeal.getId();
        setMealDishMapper.deleteBySetMealId(id);
        List<SetmealDish> setmealDishes = setmealDTO.getSetmealDishes();
        setmealDishes.forEach(dishes -> {
            dishes.setSetmealId(id);
        });
        setMealDishMapper.insertBatch(setmealDishes);
    }

    public void updateStatus(Integer status, Long id) {
        if(status == StatusConstant.ENABLE){
            List<Dish> dishes = dishMapper.getBySetMealId(id);
            if(dishes != null && !dishes.isEmpty()){
                dishes.forEach(dish -> {
                    if(StatusConstant.DISABLE.equals(dish.getStatus())){
                        throw new SetmealEnableFailedException(MessageConstant.SETMEAL_ENABLE_FAILED);
                    }
                });
            }
        }
        Setmeal setmeal = Setmeal.builder().id(id).status(status).build();
        setMealMapper.update(setmeal);
    }

    public List<Setmeal> list(Setmeal setmeal) {
        return setMealMapper.list(setmeal);
    }

    public List<DishItemVO> getDishItemById(Long id) {
        return setMealMapper.getDishItemBySetmealId(id);
    }
}
