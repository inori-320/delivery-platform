package com.lty.controller.user;

import com.lty.dto.ShoppingCartDTO;
import com.lty.result.Result;
import com.lty.service.ShoppingCartService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
