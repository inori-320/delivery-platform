package com.lty.mapper;

import com.github.pagehelper.Page;
import com.lty.annotation.AutoFill;
import com.lty.dto.SetmealPageQueryDTO;
import com.lty.entity.Setmeal;
import com.lty.entity.SetmealDish;
import com.lty.enumeration.OperationType;
import com.lty.vo.SetmealVO;
import lombok.Setter;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface SetMealMapper {

    /**
     * 根据分类id查询套餐的数量
     * @param id
     * @return
     */
    @Select("select count(id) from setmeal where category_id = #{categoryId}")
    Integer countByCategoryId(Long id);

    @AutoFill(OperationType.INSERT)
    void insert(Setmeal setmeal);

    Page<SetmealVO> pageQuery(SetmealPageQueryDTO queryDTO);

    @Select("select * from setmeal where id = #{id}")
    Setmeal getById(Long id);

    @Delete("delete from setmeal where id = #{id}")
    void deleteById(Long id);

    @AutoFill(OperationType.UPDATE)
    void update(Setmeal setmeal);
}
