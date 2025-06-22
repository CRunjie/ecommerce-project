package com.ch.ecommerce.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ch.ecommerce.entity.OrderdetailEntity;

public interface OrderDetailService extends IService<OrderdetailEntity> {
    /**
     * 根据订单ID删除订单详情
     * @param orderId 订单ID
     * @return 是否删除成功
     */
    boolean removeByOrderId(Integer orderId);
}
