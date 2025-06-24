package com.ch.ecommerce.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ch.ecommerce.common.ApiResponse;
import com.ch.ecommerce.entity.OrdersEntity;

import java.util.List;
import java.util.Map;

public interface OrdersService extends IService<OrdersEntity> {
    ApiResponse<OrdersEntity> submitOrder(OrdersEntity ordersEntity);
    ApiResponse<Map<String, Object>> getOrdersByUid(OrdersEntity ordersEntity);
    ApiResponse<Object> goPay(OrdersEntity ordersEntity);
    ApiResponse<List<Map<String,Object>>> getOrdersDetail(OrdersEntity ordersEntity);
    ApiResponse<List<OrdersEntity>> selectOrderByMonth(OrdersEntity ordersEntity);
    ApiResponse<List<Map<String, Object>>> selectOrderByType();
    ApiResponse<Map<String, Object>> getAllOrders(OrdersEntity ordersEntity);
    ApiResponse<List<Map<String, Object>>> getAllGoodsForSelect();
    
    // 用户端逻辑删除订单
    ApiResponse<Object> deleteOrderByUser(OrdersEntity ordersEntity);
}
