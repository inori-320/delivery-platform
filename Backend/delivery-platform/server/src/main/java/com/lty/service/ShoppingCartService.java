package com.lty.service;

import com.lty.dto.ShoppingCartDTO;
import com.lty.entity.ShoppingCart;

import java.util.List;

/**
 * @author lty
 */
public interface ShoppingCartService {
    void addDish(ShoppingCartDTO shoppingCartDTO);

    List<ShoppingCart> chackShoppingCart();

    void subDish(ShoppingCartDTO shoppingCartDTO);

    void cleanCart();
}
