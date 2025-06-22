package com.ch.ecommerce.common.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource(value={"classpath:conf/conf.properties"})
public class PropertyConfig {

}
