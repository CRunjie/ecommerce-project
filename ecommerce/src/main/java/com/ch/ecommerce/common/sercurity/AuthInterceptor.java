package com.ch.ecommerce.common.sercurity;

import cn.hutool.json.JSONUtil;
import com.ch.ecommerce.common.ApiResponse;
import com.ch.ecommerce.common.ResultCode;
import com.ch.ecommerce.common.sercurity.utils.HttpJwtTokenUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.lang.annotation.Annotation;

@Component
@Slf4j
@SuppressWarnings("all")
public class AuthInterceptor implements HandlerInterceptor {
	@Autowired
	private HttpJwtTokenUtil jwtHttpUtil;
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		String requestURI = request.getRequestURI();
		log.info(requestURI + " is start");
		if (!(handler instanceof HandlerMethod)) {
			return true;
		}
		
        HandlerMethod handlerMethod = (HandlerMethod) handler;
        Annotation authIgnore =  handlerMethod.getMethodAnnotation(AuthIgnore.class);
		//无需签名
        if(authIgnore != null) {
        	return true;
        }
        
        boolean res = jwtHttpUtil.validate(request);
		if(!res) {
			ApiResponse<Object> apiResponse = null;
			if(requestURI.contains("/before")) {
				apiResponse = ApiResponse.fail(ResultCode.BEFORE_UNAUTHORIZED);
			} else {
				apiResponse = ApiResponse.fail(ResultCode.UNAUTHORIZED);
			}
			response.setContentType("application/json;charset=utf-8");
			response.getWriter().write((JSONUtil.toJsonStr(apiResponse)));
			return false;
		} else {
			return true;
		}
	}
}
