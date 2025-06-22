package com.ch.ecommerce.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ch.ecommerce.entity.CartTable;

import java.util.List;
import java.util.Map;

public interface CartMapper extends BaseMapper<CartTable> {
    List<Map<String, Object>> myCartGoods(int uid);
}
