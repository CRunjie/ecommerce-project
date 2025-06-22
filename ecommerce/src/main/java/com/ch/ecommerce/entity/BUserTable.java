package com.ch.ecommerce.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@SuppressWarnings("all")
@TableName("busertable")
public class BUserTable {
    @TableId(type = IdType.AUTO)
    private Integer id;
    private String bemail;
    private String bpwd;
    @TableField(exist = false)
    private String token;
    @TableField(exist = false)
    private String code;
}
