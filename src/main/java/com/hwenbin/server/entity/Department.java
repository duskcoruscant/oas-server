package com.hwenbin.server.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.hwenbin.server.core.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author hwb
 * @create 2022-03-25
 */
@TableName("department")
@Data
@EqualsAndHashCode(callSuper = true)
public class Department extends BaseEntity {

    /**
     * 部门Id
     */
    @TableId
    private Long id;

    /**
     * 部门名称
     */
    private String name;

    /**
     * 父部门id
     */
    private Long parentId;

    /**
     * 显示顺序
     */
    private Integer sort;

    /**
     * 负责人id
     */
    private Long leaderEmpId;

    /**
     * 联系电话
     */
    private String phone;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 部门状态（0正常 1停用）
     */
    private Integer status;

}
