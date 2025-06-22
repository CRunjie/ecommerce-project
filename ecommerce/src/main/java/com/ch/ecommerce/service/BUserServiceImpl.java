package com.ch.ecommerce.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ch.ecommerce.common.ApiResponse;
import com.ch.ecommerce.common.MD5Util;
import com.ch.ecommerce.common.ResultCode;
import com.ch.ecommerce.common.sercurity.utils.ConfigurationBean;
import com.ch.ecommerce.common.sercurity.utils.JwtTokenUtil;
import com.ch.ecommerce.common.sercurity.utils.RedisUtil;
import com.ch.ecommerce.entity.BUserTable;
import com.ch.ecommerce.entity.CartTable;
import com.ch.ecommerce.entity.FocusEntity;
import com.ch.ecommerce.entity.OrdersEntity;
import com.ch.ecommerce.mapper.BUserMapper;
import com.ch.ecommerce.mapper.CartMapper;
import com.ch.ecommerce.mapper.FocusMapper;
import com.ch.ecommerce.mapper.OrdersMapper;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class BUserServiceImpl
        extends ServiceImpl<BUserMapper, BUserTable>
        implements BUserService {

    @Resource
    private JwtTokenUtil jwtUtil;
    @Resource
    private RedisUtil redisUtil;
    @Resource
    private ConfigurationBean config;
    @Resource
    private FocusMapper focusMapper;
    @Resource
    private FocusService focusService;
    @Resource
    private CartMapper cartMapper;
    @Resource
    private CartService cartService;
    @Resource
    private OrdersMapper ordersMapper;
    @Resource
    private OrdersService ordersService;
    @Resource
    private OrderDetailService orderDetailService;

    @Override
    public ApiResponse<Object> register(BUserTable bUserTable) {
        //对明文加密
        bUserTable.setBpwd(MD5Util.MD5(bUserTable.getBpwd()));
        long n =  this.lambdaQuery().eq(BUserTable::getBemail, bUserTable.getBemail()).count();
        if(n > 0 ) {//邮箱已注册
            return ApiResponse.fail(ResultCode.EMAIL_EXISTED);
        } else if(save(bUserTable)) {//注册成功
            return ApiResponse.success();
        } else {//注册失败
            return ApiResponse.fail();
        }
    }

    @Override
    public ApiResponse<BUserTable> login(BUserTable bUserTable) {
        String rand = (String)redisUtil.get("code");
        if(!rand.equalsIgnoreCase(bUserTable.getCode())) {
            // 验证码错误
            return ApiResponse.fail(ResultCode.CODE_ERROR);
        }
        //链式query
        long res = this.lambdaQuery().eq(BUserTable::getBemail, bUserTable.getBemail()).count();
        if(res == 0) {
            // 用户名不存在
            return ApiResponse.fail(ResultCode.USER_NOT_FOUND);
        }
        bUserTable.setBpwd(MD5Util.MD5(bUserTable.getBpwd()));
        List<BUserTable> mu = this.lambdaQuery()
                .eq(BUserTable::getBemail, bUserTable.getBemail())
                .eq(BUserTable::getBpwd, bUserTable.getBpwd()).list();
        if(!mu.isEmpty()){//登录成功
            BUserTable userTable = mu.get(0);
            String token = jwtUtil.createToken(userTable.getBemail());
            //签名时验证是否过期
            redisUtil.set("login_" + userTable.getBemail(), userTable.getBemail(), config.getRedisExpiration());
            userTable.setToken(token);
            return ApiResponse.success(userTable);
        }else{//密码错误
            return ApiResponse.fail(ResultCode.LOGIN_FAILED);
        }
    }
    
    @Override
    public ApiResponse<Map<String, Object>> getUsersByPage(BUserTable bUserTable) {
        // 默认每页显示5条记录
        int pageSize = bUserTable.getPageSize() != null ? bUserTable.getPageSize() : 5;
        IPage<BUserTable> iPage = new Page<>(bUserTable.getCurrentPage(), pageSize);
        
        // 构建查询条件
        QueryWrapper<BUserTable> queryWrapper = new QueryWrapper<>();
        
        // 按邮箱模糊查询
        if (StringUtils.hasText(bUserTable.getBemail())) {
            queryWrapper.like("bemail", bUserTable.getBemail());
        }
        
        // 按ID查询
        if (bUserTable.getId() != null) {
            queryWrapper.eq("id", bUserTable.getId());
        }
        
        // 执行分页查询
        IPage<BUserTable> page = page(iPage, queryWrapper);
        
        // 构造返回结果
        Map<String, Object> result = new HashMap<>();
        result.put("users", page.getRecords());
        result.put("total", page.getTotal());
        result.put("pages", page.getPages());
        result.put("current", page.getCurrent());
        
        return ApiResponse.success(result);
    }
    
    @Override
    public ApiResponse<Object> addUser(BUserTable bUserTable) {
        // 检查邮箱是否已存在
        long count = this.lambdaQuery().eq(BUserTable::getBemail, bUserTable.getBemail()).count();
        if (count > 0) {
            return ApiResponse.fail(ResultCode.EMAIL_EXISTED);
        }
        
        // 对密码进行MD5加密
        if (StringUtils.hasText(bUserTable.getBpwd())) {
            bUserTable.setBpwd(MD5Util.MD5(bUserTable.getBpwd()));
        } else {
            // 如果未提供密码，设置默认密码123456
            bUserTable.setBpwd(MD5Util.MD5("123456"));
        }
        
        // 保存用户
        if (save(bUserTable)) {
            return ApiResponse.success();
        } else {
            return ApiResponse.fail();
        }
    }
    
    @Override
    public ApiResponse<Object> updateUser(BUserTable bUserTable) {
        // 检查用户是否存在
        BUserTable existUser = getById(bUserTable.getId());
        if (existUser == null) {
            return ApiResponse.fail(ResultCode.USER_NOT_FOUND);
        }
        
        // 如果修改了邮箱，检查新邮箱是否已被使用
        if (!existUser.getBemail().equals(bUserTable.getBemail())) {
            long count = this.lambdaQuery().eq(BUserTable::getBemail, bUserTable.getBemail()).count();
            if (count > 0) {
                return ApiResponse.fail(ResultCode.EMAIL_EXISTED);
            }
        }
        
        // 如果密码不为空，则加密保存
        if (StringUtils.hasText(bUserTable.getBpwd())) {
            bUserTable.setBpwd(MD5Util.MD5(bUserTable.getBpwd()));
        } else {
            // 如果密码为空，保留原密码
            bUserTable.setBpwd(existUser.getBpwd());
        }
        
        // 更新用户
        if (updateById(bUserTable)) {
            return ApiResponse.success();
        } else {
            return ApiResponse.fail();
        }
    }
    
    @Override
    @Transactional
    public ApiResponse<Object> deleteUser(BUserTable bUserTable) {
        // 检查用户是否存在
        BUserTable existUser = getById(bUserTable.getId());
        if (existUser == null) {
            return ApiResponse.fail(ResultCode.USER_NOT_FOUND);
        }
        
        try {
            Integer userId = bUserTable.getId();
            
            // 1. 删除与该用户相关的所有关注记录
            QueryWrapper<FocusEntity> focusWrapper = new QueryWrapper<>();
            focusWrapper.eq("busertable_id", userId);
            focusService.remove(focusWrapper);
            
            // 2. 删除与该用户相关的所有购物车记录
            QueryWrapper<CartTable> cartWrapper = new QueryWrapper<>();
            cartWrapper.eq("busertable_id", userId);
            cartService.remove(cartWrapper);
            
            // 3. 处理订单相关数据
            // 查找用户相关的所有订单
            QueryWrapper<OrdersEntity> ordersWrapper = new QueryWrapper<>();
            ordersWrapper.eq("busertable_id", userId);
            List<OrdersEntity> userOrders = ordersService.list(ordersWrapper);
            
            // 删除每个订单的详情记录
            for (OrdersEntity order : userOrders) {
                // 使用orderDetailService删除订单详情
                orderDetailService.removeByOrderId(order.getId());
            }
            
            // 删除订单主记录
            ordersService.remove(ordersWrapper);
            
            // 4. 最后删除用户
            if (removeById(userId)) {
                return ApiResponse.success();
            } else {
                return ApiResponse.fail();
            }
        } catch (Exception e) {
            // 捕获可能的异常，比如其他表的外键约束
            return ApiResponse.fail(ResultCode.OPERATION_FAILED, "删除用户失败: " + e.getMessage());
        }
    }
}
