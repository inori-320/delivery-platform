package com.lty.controller.user;

import com.lty.result.Result;
import com.lty.service.ShopService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author lty
 */
@RestController("userShopController")
@Api(tags = "店铺相关接口")
@RequestMapping("/user/shop")
public class ShopController {
    @Autowired
    private ShopService shopService;

    @ApiOperation("查询营业状态")
    @GetMapping("/status")
    public Result<Integer> getStatus(){
        Integer status = shopService.getStatus();
        return Result.success(status);
    }


}
