package com.lty.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.lty.constant.JwtClaimsConstant;
import com.lty.constant.MessageConstant;
import com.lty.constant.WechatConstant;
import com.lty.dto.UserLoginDTO;
import com.lty.entity.User;
import com.lty.exception.LoginFailedException;
import com.lty.mapper.UserMapper;
import com.lty.properties.JwtProperties;
import com.lty.properties.WeChatProperties;
import com.lty.service.UserService;
import com.lty.utils.HttpClientUtil;
import com.lty.utils.JwtUtil;
import com.lty.utils.WeChatPayUtil;
import com.lty.vo.UserLoginVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * @author lty
 */
@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private JwtProperties jwtProperties;
    @Autowired
    private WeChatProperties weChatProperties;

    private String getOpenId(String code){
        // 通过HttpClient获取，调用微信接口服务，获取当前微信用户的openid
        Map<String, String> data = new HashMap<>();
        data.put("appid", weChatProperties.getAppid());
        data.put("secret", weChatProperties.getSecret());
        data.put("js_code", code);
        data.put("grant_type", WechatConstant.GRANT_TYPE);
        String json = HttpClientUtil.doGet(WechatConstant.WX_LOGIN, data);
        JSONObject jsonObject = JSON.parseObject(json);
        return jsonObject.getString("openid");
    }

    public UserLoginVO login(UserLoginDTO loginDTO) {
        String openid = getOpenId(loginDTO.getCode());
        // 如果openid为空，为登录失败，抛出业务异常
        if(openid == null || openid.isEmpty()){
            throw new LoginFailedException(MessageConstant.LOGIN_FAILED);
        }
        // 判断当前用户是否为新用户
        User user = userMapper.selectUserByOpenId(openid);
        if(user == null){
            user = User.builder().openid(openid).createTime(LocalDateTime.now()).build();
            userMapper.insertNewUser(user);
        }
        // 为微信用户生成jwt令牌
        Map<String, Object> claims = new HashMap<>();
        claims.put(JwtClaimsConstant.USER_ID, user.getId());
        String token = JwtUtil.createJWT(jwtProperties.getUserSecretKey(), jwtProperties.getUserTtl(), claims);
        return new UserLoginVO(user.getId(), user.getOpenid(), token);
    }
}
