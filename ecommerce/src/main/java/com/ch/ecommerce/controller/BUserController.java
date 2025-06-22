package com.ch.ecommerce.controller;

import cn.hutool.captcha.CaptchaUtil;
import cn.hutool.captcha.CircleCaptcha;
import com.ch.ecommerce.common.ApiResponse;
import com.ch.ecommerce.common.sercurity.AuthIgnore;
import com.ch.ecommerce.common.sercurity.utils.ConfigurationBean;
import com.ch.ecommerce.common.sercurity.utils.RedisUtil;
import com.ch.ecommerce.entity.BUserTable;
import com.ch.ecommerce.service.BUserService;
import jakarta.annotation.Resource;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/before/bUser")
public class BUserController {
    @Resource
    private BUserService bUserService;
    @Resource
    private RedisUtil redisUtil;
    @Resource
    private ConfigurationBean config;
    @AuthIgnore
    @GetMapping("/getCode")
    public void getCode(HttpServletResponse response) throws IOException {
        CircleCaptcha circleCaptcha = CaptchaUtil.createCircleCaptcha(116, 30, 4, 10);
        redisUtil.set("code",circleCaptcha.getCode(), config.getRedisExpiration());//验证码存到redis缓存中
        ServletOutputStream outputStream = response.getOutputStream();
        circleCaptcha.write(outputStream);
        outputStream.close();
    }
    @AuthIgnore
    @PostMapping("/register")
    public ApiResponse<Object> register(@RequestBody BUserTable bUserTable){
        return bUserService.register(bUserTable);
    }

    @AuthIgnore
    @PostMapping("/login")
    public ApiResponse<BUserTable> login(@RequestBody BUserTable bUserTable){
        return bUserService.login(bUserTable);
    }

}
