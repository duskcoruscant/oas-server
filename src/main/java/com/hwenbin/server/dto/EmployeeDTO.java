package com.hwenbin.server.dto;

import com.hwenbin.server.core.validation.Phone;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import java.util.Date;
import java.util.Set;

/**
 * @author hwb
 * @create 2022-03-22
 */
@Data
public class EmployeeDTO {

    /**
     * 员工id
     */
    private Long id;

    /**
     * 员工姓名
     */
    @NotEmpty(message = "员工姓名不能为空")
    private String name;

    /**
     * 员工性别 1女，2男
     */
    private Integer sex;

    /**
     * 员工电话
     */
    @Phone
    private String phone;

    /**
     * 员工邮箱
     */
    @Email(message = "邮箱格式不正确")
    private String email;

    /**
     * 员工生日
     */
    private Date birthday;

    /**
     * 入职日期
     */
    private Date entryDate;

    /**
     * 部门id
     */
    private Long deptId;

    /**
     * 部门名称
     */
    private String deptName;

    /**
     * 员工职位
     */
    private Set<Long> positionIds;

    /**
     * 职位名称，多个则拼接字符串
     */
    private String positionName;

    /**
     * 账户昵称
     */
    private String nickname;

    /**
     * 备注
     */
    private String remark;

    /**
     * 员工状态
     */
    private Integer status;

}
