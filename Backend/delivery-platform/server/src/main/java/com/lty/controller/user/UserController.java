package com.lty.controller.user;

import com.lty.dto.UserLoginDTO;
import com.lty.entity.User;
import com.lty.result.Result;
import com.lty.service.UserService;
import com.lty.vo.UserLoginVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author lty
 */
@RestController
@Api(tags = "用户相关模块")
@Slf4j
@RequestMapping("/user/user")
public class UserController {
    @Autowired
    private UserService userService;

    @PostMapping("/login")
    @ApiOperation("微信登录")
    public Result<UserLoginVO> login(@RequestBody UserLoginDTO loginDTO){
        log.info("微信登录");
        UserLoginVO loginUser = userService.login(loginDTO);
        return Result.success(loginUser);
    }
}