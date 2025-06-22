package com.ch.ecommerce.common.sercurity;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Retention(RUNTIME)
@Target({TYPE,METHOD})//注解可以应用到类、接口以及方法上
public @interface AuthIgnore {

}
