package com.ch.ecommerce.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@SuppressWarnings("all")
@TableName("ausertable")
public class AUserTable {
    @TableId(type = IdType.AUTO)
    private Integer id;
    private String aname;
    private String apwd;
    @TableField(exist = false)
    private String token;
}
