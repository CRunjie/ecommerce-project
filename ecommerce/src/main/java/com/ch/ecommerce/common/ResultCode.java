package com.ch.ecommerce.common;

import lombok.Getter;

@Getter
public enum ResultCode {

    UNAUTHORIZED(401, "未授权"),
    BEFORE_UNAUTHORIZED(402, "未授权"),
    FORBIDDEN(403, "禁止访问"),
    NOT_FOUND(404, "资源不存在"),
    METHOD_NOT_ALLOWED(405, "方法不允许"),
    EXISTED(409, "资源已存在"),
    SUCCESS(200, "操作成功"),
    ERROR(500, "系统错误"),

    LOGIN_FAILED(300, "密码错误！"),
    USER_NOT_FOUND(301, "用户不存在！"),
    USER_ALREADY_EXISTED(302, "用户已存在！"),
    GOODS_EXIST_IN_CART_OR_ORDER_OR_FOCUS(304, "商品已存在于购物车、订单或关注列表中，请勿删除！"),
    EMAIL_EXISTED(305, "邮箱已存在！"),
    CODE_ERROR(306, "验证码错误！"),
    FOCUS_ALREADY_EXISTS(307, "已关注！"),
    TYPE_DOES_NOT_DELETE(303, "类型下有商品，不能删除！");

    private final Integer code;
    private final String msg;

    ResultCode(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }
}