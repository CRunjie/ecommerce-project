package com.ch.ecommerce.mapper;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.ch.ecommerce.common.ApiResponse;
import com.ch.ecommerce.entity.GoodsEntity;
import org.apache.ibatis.annotations.Param;

import java.util.Map;

public interface GoodsMapper extends BaseMapper<GoodsEntity> {
    //分页查询商品
    IPage<GoodsEntity> iPageGoods(IPage<GoodsEntity> page,
                                  @Param(Constants.WRAPPER) LambdaQueryWrapper<GoodsEntity> wrapper);

    ApiResponse<Map<String, Object>> iPageMyFocusGoods(GoodsEntity goodsEntity);
}
