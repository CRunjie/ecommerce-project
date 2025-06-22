package com.ch.ecommerce.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ch.ecommerce.common.ApiResponse;
import com.ch.ecommerce.entity.FocusEntity;
import com.ch.ecommerce.entity.GoodsEntity;

import java.util.Map;

public interface FocusService extends IService<FocusEntity> {
  ApiResponse<Object> focus(FocusEntity focusEntity);
  ApiResponse<Map<String, Object>> iPageMyFocusGoods(GoodsEntity goodsEntity);
}
