package com.ch.ecommerce.controller;

import com.ch.ecommerce.common.ApiResponse;
import com.ch.ecommerce.entity.OrdersEntity;
import com.ch.ecommerce.service.OrdersService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/before/orders")
public class OrdersController {

    @Resource
    private OrdersService ordersService;

    @PostMapping("/submitOrder")
    public ApiResponse<OrdersEntity> submitOrder(@RequestBody OrdersEntity ordersEntity){
        return ordersService.submitOrder(ordersEntity);
    }

    @PostMapping("/getOrdersByUid")
    public ApiResponse<Map<String, Object>> getOrdersByUid(@RequestBody OrdersEntity ordersEntity){
        return ordersService.getOrdersByUid(ordersEntity);
    }
    @PostMapping("/goPay")
    public ApiResponse<Object> goPay(@RequestBody OrdersEntity ordersEntity){
        return ordersService.goPay(ordersEntity);
    }

    @PostMapping("/getOrdersDetail")
    public ApiResponse<List<Map<String,Object>>> getOrdersDetail(@RequestBody OrdersEntity ordersEntity){
        return ordersService.getOrdersDetail(ordersEntity);
    }
    
    @PostMapping("/deleteOrder")
    public ApiResponse<Object> deleteOrder(@RequestBody OrdersEntity ordersEntity){
        return ordersService.deleteOrderByUser(ordersEntity);
    }

    @PostMapping("/cancelOrder")
    public ApiResponse<Object> cancelOrder(@RequestBody OrdersEntity ordersEntity){
        return ordersService.cancelOrder(ordersEntity);
    }
}
