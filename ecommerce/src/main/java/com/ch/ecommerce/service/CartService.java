package com.ch.ecommerce.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ch.ecommerce.common.ApiResponse;
import com.ch.ecommerce.entity.CartTable;

import java.util.List;
import java.util.Map;

public interface CartService extends IService<CartTable> {
    ApiResponse<Object> addCart(CartTable cartTable);
    ApiResponse<Object> updateCart(CartTable cartTable);
    ApiResponse<Object> clearCart(CartTable cartTable);
    ApiResponse<Object> removeCart(CartTable cartTable);
    ApiResponse<List<Map<String, Object>>> myCartGoods(CartTable cartTable);
}
