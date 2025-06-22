package com.ch.ecommerce.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ch.ecommerce.common.ApiResponse;
import com.ch.ecommerce.entity.GoodsEntity;
import com.ch.ecommerce.entity.GoodsTypeEntity;

import java.util.List;
import java.util.Map;

public interface  GoodsService extends IService<GoodsEntity> {
    ApiResponse<List<GoodsTypeEntity>> getGoodsType();
    ApiResponse<Map<String, Object>> getGoodsByPage(GoodsEntity goodsEntity);
    ApiResponse<Object> addGoods(GoodsEntity goodsEntity);
    ApiResponse<Object> updateGoods(GoodsEntity goodsEntity);
    ApiResponse<Object> deleteGoods(GoodsEntity goodsEntity);
}
