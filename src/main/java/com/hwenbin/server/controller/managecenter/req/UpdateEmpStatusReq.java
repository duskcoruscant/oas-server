package com.hwenbin.server.controller.managecenter.req;

import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @author hwb
 * @create 2022-03-29
 */
@Data
public class UpdateEmpStatusReq {

    /**
     * 员工id
     */
    @NotNull
    private Long id;

    /**
     * 状态
     */
    @NotNull
    private Integer status;

}
