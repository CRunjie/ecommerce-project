package com.ch.ecommerce.common.config;

import com.ch.ecommerce.common.sercurity.AuthInterceptor;
import jakarta.annotation.Resource;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
	@Resource
	private AuthInterceptor authInter;

	public void addInterceptors(InterceptorRegistry registry) {
		System.out.println("addInterceptors");
		registry.addInterceptor(authInter).addPathPatterns("/**");
	}
}
