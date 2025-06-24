package com.ch.ecommerce.controller;

import com.ch.ecommerce.common.ApiResponse;
import com.ch.ecommerce.common.MyUtil;
import com.ch.ecommerce.common.sercurity.AuthIgnore;
import com.ch.ecommerce.entity.GoodsEntity;
import com.ch.ecommerce.entity.GoodsTypeEntity;
import com.ch.ecommerce.entity.OrdersEntity;
import com.ch.ecommerce.service.GoodsService;
import com.ch.ecommerce.service.OrdersService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/goods")
@SuppressWarnings("all")
public class GoodsController {
    private static String newFileName;
    private static byte[] fileContent;

    @Resource
    private GoodsService goodsService;
    @Resource
    private OrdersService ordersService;

    @AuthIgnore
    @PostMapping("/upload")
    public void uploadFile(MultipartFile file) {
        if (!file.isEmpty()) {
            newFileName = MyUtil.getNewFileName(file.getOriginalFilename());
            try {
                fileContent = file.getBytes();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

   @GetMapping("/getGoodsType")
   public ApiResponse<List<GoodsTypeEntity>> getGoodsType(){
        return goodsService.getGoodsType();
   }
    @PostMapping("/goodsList")
    public ApiResponse<Map<String, Object>> getGoodsByPage(@RequestBody GoodsEntity goodsEntity){
        return goodsService.getGoodsByPage(goodsEntity);
    }
    @PostMapping("/addGoods")
    ApiResponse<Object> addGoods(@RequestBody GoodsEntity goodsEntity){
        goodsEntity.setGpicture(newFileName);
        goodsEntity.setFileContent(fileContent);
        return goodsService.addGoods(goodsEntity);
    }

    @PostMapping("/updateGoods")
    ApiResponse<Object> updateGoods(@RequestBody GoodsEntity goodsEntity){
        goodsEntity.setGpicture(newFileName);
        goodsEntity.setFileContent(fileContent);
        return goodsService.updateGoods(goodsEntity);
    }

    @PostMapping("/deleteGoods")
    public ApiResponse<Object> deleteGoods(@RequestBody GoodsEntity goodsEntity){
        return goodsService.deleteGoods(goodsEntity);
    }
    @PostMapping("/selectOrderByMonth")
    public ApiResponse<List<OrdersEntity>> selectOrderByMonth(OrdersEntity ordersEntity){
        return ordersService.selectOrderByMonth(ordersEntity);
    }

    @PostMapping("/selectOrderByType")
    public ApiResponse<List<Map<String, Object>>> selectOrderByType() {
        return ordersService.selectOrderByType();
    }

    // 新增管理员查询订单接口
    @PostMapping("/getAllOrders")
    public ApiResponse<Map<String, Object>> getAllOrders(@RequestBody OrdersEntity ordersEntity) {
        return ordersService.getAllOrders(ordersEntity);
    }
    
    // 获取所有商品列表，用于订单查询下拉框
    @GetMapping("/getAllGoodsForSelect")
    public ApiResponse<List<Map<String, Object>>> getAllGoodsForSelect() {
        return ordersService.getAllGoodsForSelect();
    }
}
