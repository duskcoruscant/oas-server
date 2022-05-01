package com.hwenbin.server.controller.workflow.flowmanage.req;

import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @author hwb
 * @date 2022/04/29 19:15
 */
@Data
public class AddWorkflowFormReq {

    /**
     * 表单名称
     */
    @NotBlank(message = "表单名称不能为空")
    private String formName;

    /**
     * 表单内容
     */
    private String content;

    /**
     * 备注
     */
    private String remark;

}
