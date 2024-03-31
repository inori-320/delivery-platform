package com.lty.config;

import com.lty.properties.AliOssProperties;
import com.lty.utils.AliOssUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 配置类，用于创建AliOssUtil
 * @author lty
 */
@Configuration
@Slf4j
public class OssConfiguration {
    @Bean
    @ConditionalOnMissingBean
    public AliOssUtil aliOssUtil(AliOssProperties properties){
        log.info("创建阿里云文件上传工具类对象");
        return new AliOssUtil(properties.getEndpoint(), properties.getAccessKeyId(),
                properties.getAccessKeySecret(), properties.getBucketName());
    }
}
