package com.lty.controller.admin;

import com.lty.dto.SetmealDTO;
import com.lty.dto.SetmealPageQueryDTO;
import com.lty.entity.Dish;
import com.lty.entity.Setmeal;
import com.lty.result.PageResult;
import com.lty.result.Result;
import com.lty.service.SetMealService;
import com.lty.vo.SetmealVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.models.auth.In;
import org.apache.ibatis.annotations.Delete;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author lty
 */
@Api(tags = "套餐功能管理")
@RestController
@RequestMapping("/admin/setmeal")
public class SetMealController {
    @Autowired
    private SetMealService setMealService;

    @ApiOperation("新增套餐")
    @PostMapping
    public Result save(@RequestBody SetmealDTO setmealDTO){
        setMealService.saveWithDish(setmealDTO);
        return Result.success();
    }

    @GetMapping("/page")
    @ApiOperation("套餐分页查询")
    public Result<PageResult> page(SetmealPageQueryDTO queryDTO){
        PageResult result = setMealService.pageQuery(queryDTO);
        return Result.success(result);
    }

    @DeleteMapping
    @ApiOperation("删除套餐")
    public Result deleteOneOrBatch(@RequestParam List<Long> ids){
        setMealService.deleteOneBatch(ids);
        return Result.success();
    }

    @GetMapping("/{id}")
    @ApiOperation("查询套餐")
    public Result<SetmealVO> selectSetMealById(@PathVariable Long id){
        SetmealVO vo = setMealService.selectSetMealById(id);
        return Result.success(vo);
    }

    @PutMapping
    @ApiOperation("修改套餐")
    public Result updateSetMeal(@RequestBody SetmealDTO setmealDTO){
        setMealService.updateSetMeal(setmealDTO);
        return Result.success();
    }

    @PostMapping("/status/{status}")
    @ApiOperation("修改状态")
    public Result updateStatus(@PathVariable Integer status, Long id){
        setMealService.updateStatus(status, id);
        return Result.success();
    }
}
