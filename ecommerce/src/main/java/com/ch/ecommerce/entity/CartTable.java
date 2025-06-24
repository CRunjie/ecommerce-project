package com.ch.ecommerce.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.List;

@Data
@SuppressWarnings("all")
@TableName("carttable")
public class CartTable {
    @TableId(type = IdType.AUTO)
    private Integer id;
    private Integer busertableId;
    private Integer goodstableId;
    private Integer shoppingnum;
    private Integer selected;  // 0-未选中, 1-已选中
    @TableField(exist = false)
    private List<Integer> bshoppingnum;
    @TableField(exist = false)
    private List<Integer> bcid;
    @TableField(exist = false)
    private List<Integer> bselected;
}
