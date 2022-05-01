package com.hwenbin.server.controller.workflow.flowmanage.req;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @author hwb
 * @date 2022/04/29 19:22
 */
@Data
public class UpdateWorkflowFormReq {

    /**
     * 流程表单主键id
     */
    @NotNull(message = "主键id不能为空")
    private Long formId;

    /**
     * 流程表单名称
     */
    @NotBlank(message = "流程表单名称不能为空")
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
