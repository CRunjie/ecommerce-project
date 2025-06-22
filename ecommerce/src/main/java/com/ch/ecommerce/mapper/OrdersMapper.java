package com.ch.ecommerce.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ch.ecommerce.entity.OrdersEntity;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface OrdersMapper extends BaseMapper<OrdersEntity> {
    List<Map<String,Object>> getOrdersDetail(OrdersEntity ordersEntity);
    List<Map<String,Object>> selectOrderByType();
    
    /**
     * 通过商品ID查询包含该商品的订单
     * @param page 分页对象
     * @param ordersEntity 订单查询条件
     * @return 分页订单列表
     */
    IPage<OrdersEntity> getOrdersByGoodsId(Page<OrdersEntity> page, @Param("entity") OrdersEntity ordersEntity);
}
