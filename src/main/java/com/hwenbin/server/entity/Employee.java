package com.hwenbin.server.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.hwenbin.server.core.entity.BaseEntity;
import com.hwenbin.server.core.mybatis.type.JsonLongSetTypeHandler;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;
import java.util.Set;

/**
 * @author hwb
 * @create 2022-03-22
 */
@EqualsAndHashCode(callSuper = true)
@Data
@TableName(value = "employee", autoResultMap = true)
public class Employee extends BaseEntity {

    /**
     * 员工id
     */
    @TableId
    private Long id;

    /**
     * 员工姓名
     */
    private String name;

    /**
     * 账户昵称
     */
    private String nickname;

    /**
     * 员工性别 1女，2男
     */
    private Integer sex;

    /**
     * 员工电话
     */
    private String phone;

    /**
     * 员工邮箱
     */
    private String email;

    /**
     * 员工生日
     */
    private Date birthday;

    /**
     * 员工职位
     */
    @TableField(typeHandler = JsonLongSetTypeHandler.class)
    private Set<Long> positionIds;

    /**
     * 部门id
     */
    private Long deptId;

    /**
     * 入职日期
     */
    private Date entryDate;

    /**
     * 备注
     */
    private String remark;

    /**
     * 员工状态
     */
    private Integer status;

}
