package com.ch.ecommerce.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ch.ecommerce.common.ApiResponse;
import com.ch.ecommerce.entity.BUserTable;

public interface BUserService extends IService<BUserTable> {
    ApiResponse<Object> register(BUserTable bUserTable);
    ApiResponse<BUserTable> login(BUserTable bUserTable);
}
