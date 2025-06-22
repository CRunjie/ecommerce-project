package com.ch.ecommerce.common;

import lombok.Data;

import java.io.Serializable;

@Data
@SuppressWarnings("all")
public class ApiResponse<T> implements Serializable {

    // 状态码（枚举管理）
    private Integer code = ResultCode.SUCCESS.getCode();
    // 消息描述
    private String msg = ResultCode.SUCCESS.getMsg();
    // 业务数据
    private T resData;
    // 私有构造器
    private ApiResponse() {}
    // 成功响应静态方法，E为业务数据类型，如果不清楚时可以用Object代替
    public static <E> ApiResponse<E> success(E data) {
        return new ApiResponseBuilder().success(data).build();
    }
    // 成功响应静态方法
    public static <E> ApiResponse<E> success() {
        ResultCode resultCode = ResultCode.SUCCESS;
        return new ApiResponseBuilder().success(resultCode).build();
    }
    // 失败响应静态方法
    public static <E> ApiResponse<E> fail(ResultCode resultCode) {
        return new ApiResponseBuilder().fail(resultCode).build();
    }
    // 错误响应静态方法
    public static <E> ApiResponse<E> fail() {
        ResultCode resultCode = ResultCode.ERROR;
        return new ApiResponseBuilder().fail(resultCode).build();
    }
    // 链式调用构建器
    public static class ApiResponseBuilder {
        private ApiResponse<Object> response = new ApiResponse<>();

        public ApiResponseBuilder success(Object data) {
            response.code = ResultCode.SUCCESS.getCode();
            response.msg = ResultCode.SUCCESS.getMsg();
            response.resData = data;
            return this;
        }
        public ApiResponseBuilder fail(ResultCode resultCode) {
            response.code = resultCode.getCode();
            response.msg = resultCode.getMsg();
            return this;
        }
        public ApiResponseBuilder data(Object data) {
            response.resData = data;
            return this;
        }

        public ApiResponse build() {
            return (ApiResponse) response;
        }
    }
}