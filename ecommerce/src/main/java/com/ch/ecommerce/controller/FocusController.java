package com.ch.ecommerce.controller;

import com.ch.ecommerce.common.ApiResponse;
import com.ch.ecommerce.entity.FocusEntity;
import com.ch.ecommerce.entity.GoodsEntity;
import com.ch.ecommerce.service.FocusService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/before/focus")
public class FocusController {
    @Resource
    private FocusService focusService;
    @PostMapping("/add")
    public ApiResponse<Object> focus(@RequestBody FocusEntity focusEntity){
        return focusService.focus(focusEntity);
    }

    @PostMapping("/getMyFocusGoods")
    public ApiResponse<Map<String, Object>> iPageMyFocusGoods(@RequestBody GoodsEntity goodsEntity){
        return focusService.iPageMyFocusGoods(goodsEntity);
    }

}
