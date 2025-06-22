package com.ch.ecommerce.service;

import com.ch.ecommerce.common.ApiResponse;
import com.ch.ecommerce.entity.GoodsEntity;
import com.ch.ecommerce.entity.GoodsTypeEntity;

import java.util.List;

public interface IndexService {
    ApiResponse<List<GoodsEntity>> getAdvGoods();
    ApiResponse<List<GoodsEntity>> getGoodsIndex(GoodsEntity goodsEntity);
    ApiResponse<List<GoodsTypeEntity>> getAllGoodsType();
}
