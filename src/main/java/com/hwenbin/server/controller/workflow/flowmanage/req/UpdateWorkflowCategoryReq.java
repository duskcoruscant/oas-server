package com.hwenbin.server.controller.workflow.flowmanage.req;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @author hwb
 * @date 2022/04/29 15:07
 */
@Data
public class UpdateWorkflowCategoryReq {

    /**
     * 流程分类主键id
     */
    @NotNull(message = "主键不能为空")
    private Long categoryId;

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
     * 流程分类备注
     */
    @NotBlank(message = "流程分类备注不能为空")
    private String remark;

}
