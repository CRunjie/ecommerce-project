package com.ch.ecommerce.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ch.ecommerce.common.ApiResponse;
import com.ch.ecommerce.common.ResultCode;
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
        ordersEntity.setIsDeleted(0); // 设置为未删除状态
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
        
        // 只删除已选中的商品（已结算的商品）
        for (Integer goodsId : bgid) {
            // 构建删除条件
            LambdaQueryWrapper<CartTable> deleteWrapper = new LambdaQueryWrapper<>();
            deleteWrapper.eq(CartTable::getBusertableId, ordersEntity.getBusertableId())
                        .eq(CartTable::getGoodstableId, goodsId);
            cartService.remove(deleteWrapper);
        }
        
        return ApiResponse.success(ordersEntity);
    }

    @Override
    public ApiResponse<Map<String, Object>> getOrdersByUid(OrdersEntity ordersEntity) {
        //2为每页大小
        IPage<OrdersEntity> iPage = new Page<>(ordersEntity.getCurrentPage(), 2);
        //构造条件
        QueryWrapper<OrdersEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("busertable_id", ordersEntity.getBusertableId());
        // 只查询未被用户删除的订单
        queryWrapper.eq("is_deleted", 0);
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

    // 实现管理员查询所有订单的方法
    @Override
    public ApiResponse<Map<String, Object>> getAllOrders(OrdersEntity ordersEntity) {
        // 默认每页显示5条记录
        int pageSize = ordersEntity.getPageSize() != null ? ordersEntity.getPageSize() : 5;
        Page<OrdersEntity> page = new Page<>(ordersEntity.getCurrentPage(), pageSize);
        IPage<OrdersEntity> resultPage;
        
        try {
            // 如果有商品ID参数，使用特殊的JOIN查询
            if (ordersEntity.getGoodstableId() != null && ordersEntity.getGoodstableId() > 0) {
                resultPage = ordersMapper.getOrdersByGoodsId(page, ordersEntity);
            } else {
                // 没有商品ID参数，使用原来的查询方法
                QueryWrapper<OrdersEntity> queryWrapper = new QueryWrapper<>();
                
                // 订单金额区间查询
                if (ordersEntity.getMinAmount() != null) {
                    queryWrapper.ge("amount", ordersEntity.getMinAmount());
                }
                if (ordersEntity.getMaxAmount() != null) {
                    queryWrapper.le("amount", ordersEntity.getMaxAmount());
                }
                
                // 订单号范围查询
                if (ordersEntity.getMinId() != null) {
                    queryWrapper.ge("id", ordersEntity.getMinId());
                }
                if (ordersEntity.getMaxId() != null) {
                    queryWrapper.le("id", ordersEntity.getMaxId());
                }
                
                // 下单时间区间查询
                if (ordersEntity.getStartDate() != null && !ordersEntity.getStartDate().isEmpty()) {
                    queryWrapper.ge("orderdate", ordersEntity.getStartDate());
                }
                if (ordersEntity.getEndDate() != null && !ordersEntity.getEndDate().isEmpty()) {
                    queryWrapper.le("orderdate", ordersEntity.getEndDate());
                }
                
                // 订单支付状态查询
                if (ordersEntity.getStatus() != null) {
                    queryWrapper.eq("status", ordersEntity.getStatus());
                }
                
                // 删除状态查询 - 如果指定了isDeleted值，则按指定值查询
                if (ordersEntity.getIsDeleted() != null) {
                    queryWrapper.eq("is_deleted", ordersEntity.getIsDeleted());
                }
                // 否则查询所有订单，不管是否被用户删除
                
                // 按下单时间降序排序
                queryWrapper.orderByDesc("orderdate");
                
                // 执行分页查询
                resultPage = page(page, queryWrapper);
            }
            
            // 构造返回结果
            Map<String, Object> result = new HashMap<>();
            result.put("orders", resultPage.getRecords());
            result.put("total", resultPage.getTotal());
            result.put("pages", resultPage.getPages());
            result.put("current", resultPage.getCurrent());
            
            return ApiResponse.success(result);
        } catch (Exception e) {
            e.printStackTrace();
            return ApiResponse.fail();
        }
    }
    
    // 获取所有商品列表，用于订单查询下拉框
    @Override
    public ApiResponse<List<Map<String, Object>>> getAllGoodsForSelect() {
        List<Map<String, Object>> goodsList = new ArrayList<>();
        
        List<GoodsEntity> goods = goodsService.list();
        for (GoodsEntity good : goods) {
            Map<String, Object> item = new HashMap<>();
            item.put("id", good.getId());
            item.put("name", good.getGname());
            item.put("price", good.getGrprice());
            goodsList.add(item);
        }
        
        return ApiResponse.success(goodsList);
    }
    
    // 实现用户端逻辑删除订单的方法
    @Override
    public ApiResponse<Object> deleteOrderByUser(OrdersEntity ordersEntity) {
        // 检查订单是否存在
        OrdersEntity existOrder = getById(ordersEntity.getId());
        if (existOrder == null) {
            return ApiResponse.fail(ResultCode.NOT_FOUND, "订单不存在");
        }
        
        // 检查订单是否属于当前用户
        if (!existOrder.getBusertableId().equals(ordersEntity.getBusertableId())) {
            return ApiResponse.fail(ResultCode.FORBIDDEN, "无权操作此订单");
        }
        
        // 检查订单状态，只有已支付的订单才能删除
        if (existOrder.getStatus() != 1) {
            return ApiResponse.fail(ResultCode.OPERATION_FAILED, "只能删除已支付的订单");
        }
        
        // 执行逻辑删除
        existOrder.setIsDeleted(1);
        if (updateById(existOrder)) {
            return ApiResponse.success();
        } else {
            return ApiResponse.fail(ResultCode.OPERATION_FAILED, "删除订单失败");
        }
    }
}
