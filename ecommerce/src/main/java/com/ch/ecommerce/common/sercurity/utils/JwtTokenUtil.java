package com.ch.ecommerce.common.sercurity.utils;

import cn.hutool.core.date.DateField;
import cn.hutool.core.date.DateTime;
import cn.hutool.jwt.JWTPayload;
import cn.hutool.jwt.JWTUtil;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class JwtTokenUtil {
	@Resource
	private ConfigurationBean myConfig;
    @Resource
    private RedisUtil redisUtil;
    public String createToken(String userName) {
        DateTime now = DateTime.now();
        DateTime newTime = now.offsetNew(DateField.MINUTE, myConfig.getExpiration());
        Map<String,Object> payload = new HashMap<>();
        // 签发人
        payload.put(JWTPayload.ISSUER, myConfig.getIss());
        //签发时间
        payload.put(JWTPayload.ISSUED_AT, now);
        //过期时间
        payload.put(JWTPayload.EXPIRES_AT, newTime);
        //生效时间
        payload.put(JWTPayload.NOT_BEFORE, now);
        //载荷
        payload.put(HttpJwtTokenUtil.KEY_USERNAME, userName);
        String token = JWTUtil.createToken(payload, myConfig.getSecret().getBytes());
        return token;
    }

    /**
     * 验证token签名
     * @return
     */
    public boolean verify(String token) {
        return JWTUtil.verify(token, myConfig.getSecret().getBytes());
    }

    /**
     * 从token获取用户信息
     * @param token
     * @return
     */
    public String getUsername(String token) {
        return JWTUtil.parseToken(token).getPayload(HttpJwtTokenUtil.KEY_USERNAME).toString();
    }

    /**
     * 是否已过期, 未过期，延长时间
     * 
     * @param token
     * @return
     */
    public boolean isExpiration(String token) {
        String userName = getUsername(token);
        String key = "login_" + userName;
        boolean hasEx = redisUtil.hasKey(key);
        if(hasEx) {
            redisUtil.expire(key, myConfig.getRedisExpiration());
            return true;
        } else {
            return false;
        }
    }
}
