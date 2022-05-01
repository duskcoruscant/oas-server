package com.hwenbin.server.controller.workflow.flowmanage.req;

import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @author hwb
 * @date 2022/04/29 14:46
 */
@Data
public class AddWorkflowCategoryReq {

    /**
     * 流程分类名称
     */
    @NotBlank(message = "流程分类名称不能为空")
    private String categoryName;

    /**
     * 流程分类编码
     */
    @NotBlank(message = "流程分类编码不能为空")
    private String code;

    /**
     * 备注
     */
    @NotBlank(message = "流程分类备注不能为空")
    private String remark;

}
