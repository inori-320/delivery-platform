package com.lty.mapper;

import com.lty.dto.UserLoginDTO;
import com.lty.entity.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.Map;

/**
 * @author lty
 */
@Mapper
public interface UserMapper {

    User selectUserByOpenId(String openid);

    void insertNewUser(User user);

    @Select("select * from user where id = #{userId}")
    User getById(Long userId);

    @Select("select count(id) from user where create_time < #{end}")
    Integer countUser(Map<String, Object> map);
}
