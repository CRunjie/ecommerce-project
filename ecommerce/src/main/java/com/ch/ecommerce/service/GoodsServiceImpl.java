package com.ch.ecommerce.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ch.ecommerce.common.ApiResponse;
import com.ch.ecommerce.common.ResultCode;
import com.ch.ecommerce.entity.*;
import com.ch.ecommerce.mapper.GoodsMapper;
import com.ch.ecommerce.mapper.TypeMapper;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class GoodsServiceImpl extends
        ServiceImpl<GoodsMapper, GoodsEntity>
        implements GoodsService {

    @Resource
    private TypeMapper typeMapper;
    @Resource
    private GoodsMapper goodsMapper;
    @Resource
    private CartService cartService;
    @Resource
    private OrderDetailService orderDetailService;
    @Resource
    private FocusService focusService;



    @Override
    public ApiResponse<List<GoodsTypeEntity>> getGoodsType() {
        return ApiResponse.success(typeMapper.selectList(null));
    }

    @Override
    public ApiResponse<Map<String, Object>> getGoodsByPage(GoodsEntity goodsEntity) {
        //2为每页大小
        IPage<GoodsEntity> iPage = new Page<>(goodsEntity.getCurrentPage(), goodsEntity.getPageSize());
        //构造条件
        LambdaQueryWrapper<GoodsEntity> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        if (goodsEntity.getGoodstypeId() != null && goodsEntity.getGoodstypeId() > 0) {
            lambdaQueryWrapper.eq(GoodsEntity::getGoodstypeId, goodsEntity.getGoodstypeId());
        }
        if (goodsEntity.getGname() != null && !goodsEntity.getGname().isEmpty()) {
            lambdaQueryWrapper.like(GoodsEntity::getGname, goodsEntity.getGname());
        }
        //分页查询（商品管理进来时初始查询及条件查询）
        IPage<GoodsEntity> page = goodsMapper.iPageGoods(iPage, lambdaQueryWrapper);
        Map<String, Object> map = new HashMap<>();
        map.put("goodsList", page.getRecords());
        map.put("total", page.getTotal());
        return ApiResponse.success(map);
    }

    @Override
    public ApiResponse<Object> addGoods(GoodsEntity goodsEntity) {
        uploadFile(goodsEntity);
        if (this.save(goodsEntity)){
            return ApiResponse.success();
        }
        return ApiResponse.fail();
    }

    @Override
    public ApiResponse<Object> updateGoods(GoodsEntity goodsEntity) {
        uploadFile(goodsEntity);
        if (this.updateById(goodsEntity)){
            return ApiResponse.success();
        }
        return ApiResponse.fail();
    }

    @Override
    public ApiResponse<Object> deleteGoods(GoodsEntity goodsEntity) {
        long n1 = cartService.count(new LambdaQueryWrapper<CartTable>().eq(CartTable::getGoodstableId, goodsEntity.getId()));
        long n2 = orderDetailService.count(new LambdaQueryWrapper<OrderdetailEntity>().eq(OrderdetailEntity::getGoodstableId, goodsEntity.getId()));
        long n3 = focusService.count(new LambdaQueryWrapper<FocusEntity>().eq(FocusEntity::getGoodstableId, goodsEntity.getId()));
        if (n1 > 0 || n2 > 0 || n3 > 0) {
            return ApiResponse.fail(ResultCode.GOODS_EXIST_IN_CART_OR_ORDER_OR_FOCUS);
        }
        if (this.removeById(goodsEntity.getId())) {
            return ApiResponse.success();
        }
        return ApiResponse.fail();
    }

    //上传文件
    public void uploadFile(GoodsEntity goodsEntity) {
        if(goodsEntity.getFileContent() != null
                && goodsEntity.getFileContent().length > 0){//选择了名片的图片
            String path = "D:\\VS-workspace\\ecommerce-vue\\src\\assets";
            File file = new File(path);
            if(!file.exists()){
                file.mkdirs();
            }
            File filePath = new File(path, goodsEntity.getGpicture());
            FileOutputStream out = null;
            try {
                out = new FileOutputStream(filePath);
                out.write(goodsEntity.getFileContent());
                out.flush();
                out.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

}
