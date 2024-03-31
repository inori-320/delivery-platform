package com.lty.controller.admin;

import com.lty.constant.MessageConstant;
import com.lty.result.Result;
import com.lty.utils.AliOssUtil;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import java.util.UUID;

/**
 * @author lty
 */
@RestController
@RequestMapping("/admin/common")
@Api(tags = "通用接口")
public class CommonController {
    @Autowired
    private AliOssUtil util;

    @PostMapping("/upload")
    public Result<String> upload(MultipartFile file){
        try {
            String originalFilename = file.getOriginalFilename();
            assert originalFilename != null;
            String fileType = originalFilename.substring(originalFilename.lastIndexOf("."));
            String name = UUID.randomUUID() + fileType;
            String filePath = util.upload(file.getBytes(), name);
            return Result.success(filePath);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
