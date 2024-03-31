package com.lty.controller.admin;

import com.lty.dto.DishDTO;
import com.lty.dto.DishPageQueryDTO;
import com.lty.result.PageResult;
import com.lty.result.Result;
import com.lty.service.DishService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 菜品管理
 * @author lty
 */
@RestController
@Slf4j
@Api(tags = "菜品管理")
@RequestMapping("admin/dish")
public class DishController {
    @Autowired
    private DishService dishService;

    @PostMapping
    @ApiOperation("新增菜品")
    public Result save(@RequestBody DishDTO dishDTO){
        dishService.saveWithFlavor(dishDTO);
        return Result.success();
    }

    @GetMapping("/page")
    @ApiOperation("菜品分页查询")
    public Result<PageResult> page(DishPageQueryDTO queryDTO){
        PageResult result = dishService.pageQuery(queryDTO);
        return Result.success(result);
    }
}