package com.ch.ecommerce.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ch.ecommerce.common.ApiResponse;
import com.ch.ecommerce.entity.BUserTable;

import java.util.Map;

public interface BUserService extends IService<BUserTable> {
    ApiResponse<Object> register(BUserTable bUserTable);
    ApiResponse<BUserTable> login(BUserTable bUserTable);
    
    // 用户管理相关方法
    ApiResponse<Map<String, Object>> getUsersByPage(BUserTable bUserTable);
    ApiResponse<Object> addUser(BUserTable bUserTable);
    ApiResponse<Object> updateUser(BUserTable bUserTable);
    ApiResponse<Object> deleteUser(BUserTable bUserTable);
}
