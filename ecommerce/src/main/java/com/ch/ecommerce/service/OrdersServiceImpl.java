package com.ch.ecommerce.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ch.ecommerce.common.ApiResponse;
import com.ch.ecommerce.entity.CartTable;
import com.ch.ecommerce.entity.GoodsEntity;
import com.ch.ecommerce.entity.OrderdetailEntity;
import com.ch.ecommerce.entity.OrdersEntity;
import com.ch.ecommerce.mapper.OrdersMapper;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class OrdersServiceImpl extends ServiceImpl<OrdersMapper, OrdersEntity> implements OrdersService {
    @Resource
    private OrderDetailService orderDetailService;
    @Resource
    private GoodsService goodsService;
    @Resource
    private CartService cartService;
    @Resource
    private OrdersMapper ordersMapper;

    @Override
    @Transactional //事务管理，submitOrder方法是原子性的
    public ApiResponse<OrdersEntity> submitOrder(OrdersEntity ordersEntity) {
        ordersEntity.setStatus(0);
        ordersEntity.setOrderdate(new Date());
        //生成订单
        save(ordersEntity);
        //生成订单详情
        List<Integer> bgid = ordersEntity.getBgid();
        List<Integer> bshoppingnum = ordersEntity.getBshoppingnum();
        List<OrderdetailEntity> bods = new ArrayList<OrderdetailEntity>();
        List<GoodsEntity> ges = new ArrayList<GoodsEntity>();
        for (int i = 0; i < bgid.size(); i++) {
            OrderdetailEntity ot = new OrderdetailEntity();
            GoodsEntity ge = new GoodsEntity();
            ot.setOrderbasetableId(ordersEntity.getId());
            ot.setGoodstableId(bgid.get(i));
            ot.setShoppingnum(bshoppingnum.get(i));
            bods.add(ot);
            ge.setId(bgid.get(i));
            //减少库存
            ge.setGstore(goodsService.getById(bgid.get(i)).getGstore() - bshoppingnum.get(i));
            ges.add(ge);
        }
        orderDetailService.saveBatch(bods);
        //批量更新库存
        goodsService.updateBatchById(ges);
        //清空购物车
        CartTable cartEntity = new CartTable();
        cartEntity.setBusertableId(ordersEntity.getBusertableId());
        cartService.clearCart(cartEntity);
        return ApiResponse.success(ordersEntity);
    }

    @Override
    public ApiResponse<Map<String, Object>> getOrdersByUid(OrdersEntity ordersEntity) {
        //2为每页大小
        IPage<OrdersEntity> iPage = new Page<>(ordersEntity.getCurrentPage(), 2);
        //构造条件
        QueryWrapper<OrdersEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("busertable_id", ordersEntity.getBusertableId());
        //分页查询（商品管理进来时初始查询及条件查询）
        IPage<OrdersEntity> page = page(iPage,queryWrapper);
        Map<String, Object> myres = new HashMap<>();
        myres.put("ordersByUid", page.getRecords());
        myres.put("totalPage", page.getPages());
        return ApiResponse.success(myres);
    }

    @Override
    public ApiResponse<Object> goPay(OrdersEntity ordersEntity) {
        ordersEntity.setStatus(1);
        if(this.updateById(ordersEntity))
            return ApiResponse.success();
        return ApiResponse.fail();
    }

    @Override
    public ApiResponse<List<Map<String,Object>>> getOrdersDetail(OrdersEntity ordersEntity) {
        return ApiResponse.success(ordersMapper.getOrdersDetail(ordersEntity));
    }

    @Override
    public ApiResponse<List<OrdersEntity>> selectOrderByMonth(OrdersEntity ordersEntity) {
        //默认查询所有订单
        if(ordersEntity.getStartDate() == null)
            ordersEntity.setStartDate("1900-01");
        if(ordersEntity.getEndDate() == null)
            ordersEntity.setEndDate("9000-12");
        List<OrdersEntity> ordersEntityList = query()
                .select("sum(amount) totalamount","date_format(orderdate,'%Y-%m') months")
                .eq("status", 1)
                .between("date_format(orderdate,'%Y-%m')", ordersEntity.getStartDate(), ordersEntity.getEndDate())
                .groupBy("months")
                .orderByAsc("months")
                .list();
        return ApiResponse.success(ordersEntityList);
    }

    @Override
    public ApiResponse<List<Map<String, Object>>> selectOrderByType() {
        return ApiResponse.success(ordersMapper.selectOrderByType());
    }
}
