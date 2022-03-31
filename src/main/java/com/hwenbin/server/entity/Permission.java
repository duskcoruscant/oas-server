package com.hwenbin.server.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * @author hwb
 * @create 2022-03-14
 */
@TableName("permission")
@Data
public class Permission {

    /**
     * 权限id
     */
    @TableId
    private Long id;

    /**
     * 权限名称
     */
    private String name;

    /**
     * 权限标识,对应代码中@hasAuthority(xx)
     */
    private String code;

    /**
     * 父权限id
     */
    private Long parentId;

}
