package com.hwenbin.server.dto.flow;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * 流程设计业务对象
 *
 * @author hwb
 * @date 2022/04/30 21:06
 */
@Data
public class WorkflowDesignerDTO {

    /**
     * 流程名称
     */
    @NotNull(message = "流程名称不能为空")
    private String name;

    /**
     * 流程分类
     */
    @NotBlank(message = "流程分类不能为空")
    private String category;

    /**
     * XML字符串
     */
    @NotBlank(message = "XML字符串不能为空")
    private String xml;

}
