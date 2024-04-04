package com.lty.controller.admin;

import com.lty.dto.DishDTO;
import com.lty.dto.DishPageQueryDTO;
import com.lty.entity.Dish;
import com.lty.result.PageResult;
import com.lty.result.Result;
import com.lty.service.DishService;
import com.lty.vo.DishVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.models.auth.In;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

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
    @Autowired
    private RedisTemplate redisTemplate;

    @PostMapping
    @ApiOperation("新增菜品")
    public Result save(@RequestBody DishDTO dishDTO){
        dishService.saveWithFlavor(dishDTO);
        cleanCache("dish_" + dishDTO.getCategoryId());
        return Result.success();
    }

    @GetMapping("/page")
    @ApiOperation("菜品分页查询")
    public Result<PageResult> page(DishPageQueryDTO queryDTO){
        PageResult result = dishService.pageQuery(queryDTO);
        return Result.success(result);
    }

    @DeleteMapping
    @ApiOperation("删除菜品")
    public Result deleteOneOrBatch(@RequestParam List<Long> ids){
        dishService.deleteOneOrBatch(ids);
        cleanCache("dish_*");
        return Result.success();
    }

    @GetMapping("/{id}")
    @ApiOperation("根据菜品ID查询菜品")
    public Result<DishVO> selectDishById(@PathVariable Long id){
        DishVO dishVO = dishService.getByIdWithFlavor(id);
        return Result.success(dishVO);
    }

    @PutMapping
    @ApiOperation("修改菜品")
    public Result updateDish(@RequestBody DishDTO dishDTO){
        dishService.updateDish(dishDTO);
        cleanCache("dish_*");
        return Result.success();
    }

    @GetMapping("/list")
    @ApiOperation("根据分类ID查询菜品")
    public Result<List<Dish>> selectDishByCategoryId(Long categoryId){
        List<Dish> list = dishService.selectDish(categoryId);
        return Result.success(list);
    }

    @PostMapping("/status/{status}")
    @ApiOperation("菜品状态修改")
    public Result<String> updateDishStatus(@PathVariable Integer status, Long id){
        dishService.updateDishStatus(status, id);
        cleanCache("dish_*");
        return Result.success();
    }

    private void cleanCache(String pattern){
        Set keys = redisTemplate.keys(pattern);
        redisTemplate.delete(keys);
    }
}
