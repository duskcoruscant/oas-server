package com.hwenbin.server.dto;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

/**
 * @author hwb
 * @create 2022-03-30
 */
@Data
public class RoleDTO {

    /**
     * 角色Id
     */
    private Long id;

    /**
     * 角色名称
     */
    @NotEmpty(message = "角色名不能为空")
    private String name;

    /**
     * 角色标识
     */
    @NotEmpty(message = "角色标识不能为空")
    private String code;

    /**
     * 显示顺序
     */
    @NotNull(message = "显示顺序不能为空")
    private Integer sort;

    /**
     * 备注
     */
    private String remark;

    /**
     * 角色状态
     */
    private Integer status;

}
