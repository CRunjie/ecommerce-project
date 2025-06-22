package com.ch.ecommerce.controller;

import com.ch.ecommerce.common.ApiResponse;
import com.ch.ecommerce.entity.GoodsTypeEntity;
import com.ch.ecommerce.service.TypeService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
public class TypeController {
    @Resource
    private TypeService typeService;
    @PostMapping("/types")
    public ApiResponse<Map<String, Object>> getAllTypes(
            @RequestBody GoodsTypeEntity type){
        return typeService.getAllTypes(type);
    }
    @PostMapping("/addType")
    public ApiResponse<Object> addType(@RequestBody GoodsTypeEntity type) {
        return typeService.addType(type);
    }
    @PostMapping("/deleteType")
    public ApiResponse<Object> deleteType(@RequestBody GoodsTypeEntity type){
        return typeService.deleteType(type);
    }
    @PostMapping("/editType")
    public ApiResponse<Object> editType(@RequestBody GoodsTypeEntity type) {
        return typeService.editType(type);
    }
}
