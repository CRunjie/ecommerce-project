package com.ch.ecommerce.controller;

import com.ch.ecommerce.common.ApiResponse;
import com.ch.ecommerce.entity.CartTable;
import com.ch.ecommerce.service.CartService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/before/cart")
public class CartController {
    @Resource
    private CartService cartService;
    @RequestMapping("/add")
    public ApiResponse<Object> addCart(@RequestBody CartTable cartTable) {
        return cartService.addCart(cartTable);
    }
    @RequestMapping("/bupDateCart")
    public ApiResponse<Object> updateCart(@RequestBody CartTable cartTable) {
        return cartService.updateCart(cartTable);
    }
    @RequestMapping("/clearCart")
    public ApiResponse<Object> clearCart(@RequestBody CartTable cartTable) {
        return cartService.clearCart(cartTable);
    }
    @RequestMapping("/removeCart")
    public ApiResponse<Object> removeCart(@RequestBody CartTable cartTable) {
        return cartService.removeCart(cartTable);
    }
    @RequestMapping("/myCart")
    public ApiResponse<List<Map<String, Object>>> myCartGoods(@RequestBody CartTable cartTable) {
        return cartService.myCartGoods(cartTable);
    }
    @RequestMapping("/updateSelected")
    public ApiResponse<Object> updateCartSelected(@RequestBody CartTable cartTable) {
        return cartService.updateCartSelected(cartTable);
    }
    @RequestMapping("/getSelectedGoods")
    public ApiResponse<List<Map<String, Object>>> getSelectedCartGoods(@RequestBody CartTable cartTable) {
        return cartService.getSelectedCartGoods(cartTable);
    }
}
