package com.lty.service.impl;

import com.lty.context.BaseContext;
import com.lty.dto.ShoppingCartDTO;
import com.lty.entity.Dish;
import com.lty.entity.Setmeal;
import com.lty.entity.ShoppingCart;
import com.lty.mapper.DishMapper;
import com.lty.mapper.SetMealMapper;
import com.lty.mapper.ShoppingCartMapper;
import com.lty.service.ShoppingCartService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author lty
 */
@Service
public class ShoppingCartServiceImpl implements ShoppingCartService{
    @Autowired
    private ShoppingCartMapper shoppingCartMapper;
    @Autowired
    private DishMapper dishMapper;
    @Autowired
    private SetMealMapper setMealMapper;

    public void addDish(ShoppingCartDTO shoppingCartDTO) {
        ShoppingCart shoppingCart = new ShoppingCart();
        BeanUtils.copyProperties(shoppingCartDTO, shoppingCart);
        shoppingCart.setUserId(BaseContext.getCurrentId());
        List<ShoppingCart> shoppingCarts = shoppingCartMapper.shopIsExist(shoppingCart);
        if(shoppingCarts != null && !shoppingCarts.isEmpty()){
            ShoppingCart s = shoppingCarts.get(0);
            s.setNumber(s.getNumber() + 1);
            shoppingCartMapper.updateShoppingCart(s);
        } else {
            shoppingCart.setCreateTime(LocalDateTime.now());
            shoppingCart.setNumber(1);
            Long dishId = shoppingCart.getDishId();
            if(dishId != null){
                Dish dish = dishMapper.getById(dishId);
                shoppingCart.setAmount(dish.getPrice());
                shoppingCart.setImage(dish.getImage());
                shoppingCart.setName(dish.getName());
            } else {
                Long setMealId = shoppingCart.getSetmealId();
                Setmeal setmeal = setMealMapper.getById(setMealId);
                shoppingCart.setAmount(setmeal.getPrice());
                shoppingCart.setImage(setmeal.getImage());
                shoppingCart.setName(setmeal.getName());
            }
            shoppingCartMapper.insert(shoppingCart);
        }
    }

    public void subDish(ShoppingCartDTO shoppingCartDTO) {
        ShoppingCart shoppingCart = new ShoppingCart();
        BeanUtils.copyProperties(shoppingCartDTO, shoppingCart);
    }

    public void cleanCart() {
        shoppingCartMapper.cleanCartByUserId(BaseContext.getCurrentId());
    }

    public List<ShoppingCart> chackShoppingCart() {
        ShoppingCart shoppingCart = ShoppingCart.builder().userId(BaseContext.getCurrentId()).build();
        return shoppingCartMapper.shopIsExist(shoppingCart);
    }
}
