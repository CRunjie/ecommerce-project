package com.ch.ecommerce.common.sercurity.utils;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Getter
public class ConfigurationBean {
    @Value("${token.header}")
    private String tokenHeader;
    @Value("${token.secret}")
    private String secret;
    @Value("${token.iss}")
    private String iss;
    @Value("${token.expiration}")
    private int expiration;
    @Value("${redis.expiration}")
    private long redisExpiration;
}
