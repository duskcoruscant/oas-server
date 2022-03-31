package com.hwenbin.server.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.hwenbin.server.core.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotEmpty;

/**
 * @author hwb
 * @create 2022-03-14
 */
@TableName("role")
@Data
@EqualsAndHashCode(callSuper = true)
public class Role extends BaseEntity {

    /**
     * 角色Id
     */
    @TableId
    private Long id;

    /**
     * 角色名称
     */
    private String name;

    /**
     * 角色权限字符串
     */
    private String code;

    /**
     * 显示顺序
     */
    private Integer sort;

    /**
     * 备注
     */
    private String remark;

    /**
     * 角色状态
     */
    private Integer status;

    /**
     * 角色类型 0内置，1自定义
     */
    private Integer type;

}
