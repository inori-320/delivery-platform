package com.lty.service.impl;

import com.lty.mapper.ShopMapper;
import com.lty.service.ShopService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

/**
 * @author lty
 */
@Service
@Slf4j
public class ShopServiceImpl implements ShopService {
    @Autowired
    private ShopMapper shopMapper;
    @Autowired
    private RedisTemplate redisTemplate;

    public void setShopStatus(Integer status) {
        redisTemplate.opsForValue().set("SHOP_STATUS", status);
    }

    @Override
    public Integer getStatus() {
        return (Integer) redisTemplate.opsForValue().get("SHOP_STATUS");
    }
}
