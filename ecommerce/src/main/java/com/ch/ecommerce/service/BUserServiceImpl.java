package com.ch.ecommerce.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ch.ecommerce.common.ApiResponse;
import com.ch.ecommerce.common.MD5Util;
import com.ch.ecommerce.common.ResultCode;
import com.ch.ecommerce.common.sercurity.utils.ConfigurationBean;
import com.ch.ecommerce.common.sercurity.utils.JwtTokenUtil;
import com.ch.ecommerce.common.sercurity.utils.RedisUtil;
import com.ch.ecommerce.entity.BUserTable;
import com.ch.ecommerce.mapper.BUserMapper;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

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
}
