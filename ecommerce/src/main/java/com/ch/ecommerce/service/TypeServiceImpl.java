package com.ch.ecommerce.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ch.ecommerce.common.ApiResponse;
import com.ch.ecommerce.common.ResultCode;
import com.ch.ecommerce.entity.GoodsEntity;
import com.ch.ecommerce.entity.GoodsTypeEntity;
import com.ch.ecommerce.mapper.TypeMapper;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class TypeServiceImpl
        extends ServiceImpl<TypeMapper, GoodsTypeEntity>
        implements TypeService {

    @Resource
    private GoodsService goodsService;

    @Override
    public ApiResponse<Map<String, Object>> getAllTypes(GoodsTypeEntity type) {
        //分页查询
        IPage<GoodsTypeEntity> page = new Page<>(type.getPageNum(),
                type.getPageSize());
        IPage<GoodsTypeEntity> result = this.page(page, null);
        List<GoodsTypeEntity> types = result.getRecords();
        Map<String, Object> map = new HashMap<>();
        map.put("total", result.getTotal());
        map.put("list", types);
        return ApiResponse.success(map);
    }

    @Override
    public ApiResponse<Object> addType(GoodsTypeEntity type) {
        if (this.save(type)) {
            return ApiResponse.success();
        }
        return ApiResponse.fail();
    }

    @Override
    public ApiResponse<Object> deleteType(GoodsTypeEntity type) {
        long count = goodsService.lambdaQuery().eq(GoodsEntity::getGoodstypeId, type.getId()).count();
        if (count > 0) {
            return ApiResponse.fail(ResultCode.TYPE_DOES_NOT_DELETE);
        } else if (this.removeById(type.getId())) {
            return ApiResponse.success();
        }
        return ApiResponse.fail();
    }

    @Override
    public ApiResponse<Object> editType(GoodsTypeEntity type) {
        if (this.updateById(type)) {
            return ApiResponse.success();
        }
        return ApiResponse.fail();
    }
}
