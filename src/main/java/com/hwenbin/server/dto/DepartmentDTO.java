package com.hwenbin.server.dto;

import com.hwenbin.server.core.validation.Phone;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

/**
 * @author hwb
 * @create 2022-03-25
 */
@Data
public class DepartmentDTO {

    /**
     * 部门Id
     */
    private Long id;

    /**
     * 部门名称
     */
    @NotEmpty(message = "部门名称不能为空")
    private String name;

    /**
     * 父部门id
     */
    private Long parentId;

    /**
     * 显示顺序
     */
    @NotNull(message = "显示顺序不能为空")
    private Integer sort;

    /**
     * 负责人id
     */
    private Long leaderEmpId;

    /**
     * 联系电话
     */
    @Phone
    private String phone;

    /**
     * 邮箱
     */
    @Email
    private String email;

    /**
     * 部门状态（0正常 1停用）
     */
    @NotNull(message = "部门状态不能为空")
    private Integer status;

}
