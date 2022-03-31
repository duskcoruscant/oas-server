package com.hwenbin.server.controller.managecenter.req;

import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @author hwb
 * @create 2022-03-31
 */
@Data
public class UpdateRoleStatusReq {

    /**
     * 角色id
     */
    @NotNull(message = "角色id不能为空")
    private Long id;

    /**
     * 角色状态
     */
    @NotNull(message = "角色状态不能为空")
    private Integer status;

}
