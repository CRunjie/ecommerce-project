package com.ch.ecommerce.controller;

import com.ch.ecommerce.common.ApiResponse;
import com.ch.ecommerce.common.sercurity.AuthIgnore;
import com.ch.ecommerce.entity.AUserTable;
import com.ch.ecommerce.service.AUserService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AUserController {
    @Resource
    private AUserService aUserService;

    @PostMapping("/login")
    @AuthIgnore
    public ApiResponse<AUserTable> login(@RequestBody
                                       AUserTable aUserTable){
        return aUserService.login(aUserTable);
    }

}
