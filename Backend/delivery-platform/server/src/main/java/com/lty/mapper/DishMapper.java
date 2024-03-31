package com.lty.mapper;

import com.github.pagehelper.Page;
import com.lty.annotation.AutoFill;
import com.lty.dto.DishPageQueryDTO;
import com.lty.entity.Dish;
import com.lty.enumeration.OperationType;
import com.lty.vo.DishVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface DishMapper {

    /**
     * 根据分类id查询菜品数量
     * @param categoryId
     * @return
     */
    @Select("select count(id) from dish where category_id = #{categoryId}")
    Integer countByCategoryId(Long categoryId);

    @AutoFill(OperationType.INSERT)
    void insertNewDish(Dish dish);

    Page<DishVO> pageQuery(DishPageQueryDTO queryDTO);
}
