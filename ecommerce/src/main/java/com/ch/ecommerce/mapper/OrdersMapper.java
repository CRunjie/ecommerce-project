package com.ch.ecommerce.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ch.ecommerce.entity.OrdersEntity;

import java.util.List;
import java.util.Map;

public interface OrdersMapper extends BaseMapper<OrdersEntity> {
    List<Map<String,Object>> getOrdersDetail(OrdersEntity ordersEntity);
    List<Map<String,Object>> selectOrderByType();
}
