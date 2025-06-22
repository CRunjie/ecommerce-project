package com.ch.ecommerce.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ch.ecommerce.common.ApiResponse;
import com.ch.ecommerce.entity.AUserTable;

public interface AUserService extends IService<AUserTable> {
    // 登录，当处理结果不知返回什么类型时，可以用Object作为返回类型
    ApiResponse<AUserTable> login(AUserTable aUserTable);

}
