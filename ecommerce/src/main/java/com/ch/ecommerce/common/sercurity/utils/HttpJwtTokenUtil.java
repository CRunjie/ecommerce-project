package com.ch.ecommerce.common.sercurity.utils;

import cn.hutool.core.util.StrUtil;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Component;

import jakarta.servlet.http.HttpServletRequest;


@Component
@Slf4j
public class HttpJwtTokenUtil {
	public static final String KEY_USERNAME = "userName";
	@Resource
	private ConfigurationBean jwtConfig;
	@Resource
	private JwtTokenUtil jwtUtil;

	public boolean validate(HttpServletRequest request) {
		final String requestTokenHeader = request.getHeader(jwtConfig.getTokenHeader());
		if(StrUtil.isEmpty(requestTokenHeader)) {
			return false;
		}
		if(!jwtUtil.verify(requestTokenHeader)) {
			return false;
		}
		return jwtUtil.isExpiration(requestTokenHeader);
	}
}
