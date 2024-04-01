package com.lty.mapper;

import com.github.pagehelper.Page;
import com.lty.annotation.AutoFill;
import com.lty.dto.SetmealPageQueryDTO;
import com.lty.entity.Setmeal;
import com.lty.enumeration.OperationType;
import com.lty.vo.SetmealVO;
import lombok.Setter;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

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
}
