package com.ch.ecommerce.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ch.ecommerce.common.ApiResponse;
import com.ch.ecommerce.entity.CartTable;
import com.ch.ecommerce.mapper.CartMapper;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class CartServiceImpl
        extends ServiceImpl<CartMapper, CartTable>
        implements CartService {
    @Resource
    private CartMapper cartMapper;

    @Override
    public ApiResponse<Object> addCart(CartTable cartTable) {
        Long n = this.lambdaQuery()
                .eq(CartTable::getBusertableId, cartTable.getBusertableId())
                .eq(CartTable::getGoodstableId, cartTable.getGoodstableId())
                .count();
        boolean b = false;
        if (n > 0) {//购物车中用户已购买该商品，更新购物车
            //更新的条件构造器
            UpdateWrapper<CartTable> updateWrapper = new UpdateWrapper<CartTable>();
            updateWrapper.setSql("shoppingnum = shoppingnum + " + cartTable.getShoppingnum());
            updateWrapper.last(" where busertable_id = " + cartTable.getBusertableId() +
                    " and goodstable_id = " + cartTable.getGoodstableId());
            b = this.update(updateWrapper);
        } else {
            b = save(cartTable);
        }
        if (b)//成功
            return ApiResponse.success();
        else
            return ApiResponse.fail();
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public ApiResponse<Object> updateCart(CartTable cartTable) {
        List<Integer> bCid = cartTable.getBcid();
        List<Integer> bShoppingNum = cartTable.getBshoppingnum();
        List<CartTable> bCarts = new ArrayList<CartTable>();
        for (int i = 0; i < bCid.size(); i++) {
            CartTable ce = new CartTable();
            ce.setId(bCid.get(i));
            ce.setShoppingnum(bShoppingNum.get(i));
            bCarts.add(ce);
        }
        this.updateBatchById(bCarts);//批量更新
        return ApiResponse.success();
    }

    @Override
    public ApiResponse<Object> clearCart(CartTable cartTable) {
        LambdaQueryWrapper<CartTable> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(CartTable::getBusertableId, cartTable.getBusertableId());
        boolean b = this.remove(lambdaQueryWrapper);
        if (b)
            return ApiResponse.success();
        else
            return ApiResponse.fail();
    }

    @Override
    public ApiResponse<Object> removeCart(CartTable cartTable) {
       boolean b = removeById(cartTable);
        if (b)
            return ApiResponse.success();
        else
            return ApiResponse.fail();
    }

    @Override
    public ApiResponse<List<Map<String, Object>>> myCartGoods(CartTable cartTable) {
        List<Map<String, Object>> cartGoods = cartMapper.myCartGoods(cartTable.getBusertableId());
        return ApiResponse.success(cartGoods);
    }
}
