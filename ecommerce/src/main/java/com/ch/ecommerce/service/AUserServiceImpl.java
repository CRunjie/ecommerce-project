package com.ch.ecommerce.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ch.ecommerce.common.ApiResponse;
import com.ch.ecommerce.common.ResultCode;
import com.ch.ecommerce.common.sercurity.utils.ConfigurationBean;
import com.ch.ecommerce.common.sercurity.utils.JwtTokenUtil;
import com.ch.ecommerce.common.sercurity.utils.RedisUtil;
import com.ch.ecommerce.entity.AUserTable;
import com.ch.ecommerce.mapper.AUserMapper;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

@Service
public class AUserServiceImpl
        extends ServiceImpl<AUserMapper, AUserTable>
        implements AUserService {
    @Resource
    private JwtTokenUtil jwtUtil;
    @Resource
    private RedisUtil redisUtil;
    @Resource
    private ConfigurationBean config;
    @Override
    public ApiResponse<AUserTable> login(AUserTable aUserTable) {
        long count = this.lambdaQuery().eq(AUserTable::getAname,
                aUserTable.getAname()).count();
        if (count > 0) {
            AUserTable user = this.lambdaQuery().eq(AUserTable::getAname,
                    aUserTable.getAname()).eq(AUserTable::getApwd,
                    aUserTable.getApwd()).one();
            if (user!= null) {
                String token = jwtUtil.createToken(user.getId() + "");
                //签名时验证是否过期
                redisUtil.set("login_" + user.getId(),
                        user.getId() + "",
                        config.getRedisExpiration());
                user.setToken(token);
                return ApiResponse.success(user);
            } else {
                return ApiResponse.fail(ResultCode.LOGIN_FAILED);
            }
        } else {
            return ApiResponse.fail(ResultCode.USER_NOT_FOUND);
        }
    }
}
