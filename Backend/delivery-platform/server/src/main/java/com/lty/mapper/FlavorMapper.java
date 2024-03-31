package com.lty.mapper;

import com.lty.entity.DishFlavor;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author lty
 */
@Mapper
public interface FlavorMapper {

    void insertBatch(List<DishFlavor> flavors);
}
