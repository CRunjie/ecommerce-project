package com.ch.ecommerce.controller;

import com.ch.ecommerce.common.ApiResponse;
import com.ch.ecommerce.common.sercurity.AuthIgnore;
import com.ch.ecommerce.entity.GoodsEntity;
import com.ch.ecommerce.entity.GoodsTypeEntity;
import com.ch.ecommerce.service.IndexService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/before/index")
public class IndexController {
    @Resource
    private IndexService indexService;

    @GetMapping("/getAdvGoods")
    @AuthIgnore
    ApiResponse<List<GoodsEntity>> getAdvGoods(){
        return indexService.getAdvGoods();
    }
    @GetMapping("/getAllGoodsType")
    @AuthIgnore
    ApiResponse<List<GoodsTypeEntity>> getAllGoodsType(){
        return indexService.getAllGoodsType();
    }

    @PostMapping("/getGoodsIndex")
    @AuthIgnore
    ApiResponse<List<GoodsEntity>> getGoodsIndex(@RequestBody GoodsEntity goodsEntity){
        return indexService.getGoodsIndex(goodsEntity);
    }

}
