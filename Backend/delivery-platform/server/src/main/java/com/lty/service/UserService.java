package com.lty.service;

import com.lty.dto.UserLoginDTO;
import com.lty.entity.User;
import com.lty.vo.UserLoginVO;

/**
 * @author lty
 */
public interface UserService {
    UserLoginVO login(UserLoginDTO loginDTO);
}
