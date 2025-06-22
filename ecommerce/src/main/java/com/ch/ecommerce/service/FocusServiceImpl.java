package com.ch.ecommerce.service;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ch.ecommerce.common.ApiResponse;
import com.ch.ecommerce.common.ResultCode;
import com.ch.ecommerce.entity.FocusEntity;
import com.ch.ecommerce.entity.GoodsEntity;
import com.ch.ecommerce.mapper.FocusMapper;
import com.ch.ecommerce.mapper.GoodsMapper;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class FocusServiceImpl
        extends ServiceImpl<FocusMapper, FocusEntity>
        implements FocusService {
    @Resource
    private FocusMapper focusMapper;

    @Override
    public ApiResponse<Object> focus(FocusEntity focusEntity) {
        long m = lambdaQuery()
               .eq(FocusEntity::getGoodstableId, focusEntity.getGoodstableId())
               .eq(FocusEntity::getBusertableId, focusEntity.getBusertableId())
               .count();
        if(m > 0)
            return ApiResponse.fail(ResultCode.FOCUS_ALREADY_EXISTS);
        focusEntity.setFocustime(new Date());
        if (save(focusEntity))
            return ApiResponse.success();
        return ApiResponse.fail();
    }

    @Override
    public ApiResponse<Map<String, Object>> iPageMyFocusGoods(GoodsEntity goodsEntity) {
        //2为每页大小
        IPage<GoodsEntity> iPage = new Page<>(goodsEntity.getCurrentPage(), 2);
        //构造条件
        QueryWrapper<FocusEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("ft.busertable_id", goodsEntity.getBusertableId());
        //分页查询
        IPage<GoodsEntity> page = focusMapper.iPageMyFocusGoods(iPage, queryWrapper);
        Map<String, Object> myres = new HashMap<>();
        myres.put("allGoods", page.getRecords());
        myres.put("totalPage", page.getPages());
        return ApiResponse.success(myres);
    }
}
