package com.ch.ecommerce.mapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.ch.ecommerce.entity.FocusEntity;
import com.ch.ecommerce.entity.GoodsEntity;
import org.apache.ibatis.annotations.Param;

import java.util.Map;

public interface FocusMapper extends BaseMapper<FocusEntity> {
    IPage<GoodsEntity> iPageMyFocusGoods(IPage<GoodsEntity> page, @Param(Constants.WRAPPER) QueryWrapper<FocusEntity> wrapper);
}
