package com.ch.ecommerce.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ch.ecommerce.entity.OrderdetailEntity;
import com.ch.ecommerce.mapper.OrderDetailMapper;
import org.springframework.stereotype.Service;

@Service
public class OrderDetailServiceImpl
        extends ServiceImpl<OrderDetailMapper, OrderdetailEntity>
        implements OrderDetailService {
}
