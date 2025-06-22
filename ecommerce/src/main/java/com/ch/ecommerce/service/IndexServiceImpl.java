package com.ch.ecommerce.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.ch.ecommerce.common.ApiResponse;
import com.ch.ecommerce.entity.GoodsTypeEntity;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import com.ch.ecommerce.entity.GoodsEntity;

import java.util.List;


@Service
public class IndexServiceImpl implements IndexService {
    @Resource
    private GoodsService goodsService;
    @Resource
    private TypeService typeService;
    /**
     * 获取首页广告商品
     */
    @Override
    public ApiResponse<List<GoodsEntity>> getAdvGoods() {
        //查询广告商品
        List<GoodsEntity> listAdv = goodsService.lambdaQuery().eq(GoodsEntity::getIsAdvertisement, 1)
                .orderByDesc(GoodsEntity::getId).last("limit 5").list();
        return ApiResponse.success(listAdv);
    }
    /**
     * 获取商品列表
     */
    @Override
    public ApiResponse<List<GoodsEntity>> getGoodsIndex(GoodsEntity goodsEntity) {
        //构造条件
        LambdaQueryWrapper<GoodsEntity> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        if(goodsEntity.getGoodstypeId() != null && goodsEntity.getGoodstypeId() != 0)
            lambdaQueryWrapper.eq(GoodsEntity::getGoodstypeId,goodsEntity.getGoodstypeId());
        if(goodsEntity.getGname() != null && !goodsEntity.getGname().trim().isEmpty())
            lambdaQueryWrapper.like(GoodsEntity::getGname, goodsEntity.getGname());
        lambdaQueryWrapper.orderByDesc(GoodsEntity::getId).last("limit 15");
        //执行查询
        List<GoodsEntity> listIndex = goodsService.list(lambdaQueryWrapper);
        return ApiResponse.success(listIndex);
    }

    @Override
    public ApiResponse<List<GoodsTypeEntity>> getAllGoodsType() {
        return ApiResponse.success(typeService.list());
    }
}
