package com.lty.controller.user;

import com.lty.dto.ShoppingCartDTO;
import com.lty.entity.ShoppingCart;
import com.lty.result.Result;
import com.lty.service.ShoppingCartService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.License;
import org.apache.ibatis.annotations.Delete;
import org.apache.logging.log4j.message.ReusableMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author lty
 */
@RestController
@RequestMapping("/user/shoppingCart")
@Api(tags = "购物车相关接口")
public class ShoppingCartController {
    @Autowired
    private ShoppingCartService shoppingCartService;

    @ApiOperation("添加购物车")
    @PostMapping("/add")
    public Result addDish(@RequestBody ShoppingCartDTO shoppingCartDTO){
        shoppingCartService.addDish(shoppingCartDTO);
        return Result.success();
    }

    @ApiOperation("减少购物车物品")
    @PostMapping("/sub")
    public Result subDish(@RequestBody ShoppingCartDTO shoppingCartDTO){
        shoppingCartService.subDish(shoppingCartDTO);
        return Result.success();
    }

    @ApiOperation("查看购物车")
    @GetMapping("/list")
    public Result<List<ShoppingCart>> checkShoppingCart(){
        List<ShoppingCart> carts = shoppingCartService.chackShoppingCart();
        return Result.success(carts);
    }

    @ApiOperation("清空购物车")
    @DeleteMapping("/clean")
    public Result cleanCart(){
        shoppingCartService.cleanCart();
        return Result.success();
    }
}
