package com.ch.ecommerce.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ch.ecommerce.common.ApiResponse;
import com.ch.ecommerce.entity.GoodsTypeEntity;

import java.util.List;
import java.util.Map;

public interface TypeService extends IService<GoodsTypeEntity> {
    ApiResponse<Map<String, Object>> getAllTypes(GoodsTypeEntity type);
    ApiResponse<Object> addType(GoodsTypeEntity type);
    ApiResponse<Object> deleteType(GoodsTypeEntity type);
    ApiResponse<Object> editType(GoodsTypeEntity type);
}
