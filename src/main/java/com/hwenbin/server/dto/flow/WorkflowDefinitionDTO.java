package com.hwenbin.server.dto.flow;

import lombok.Data;

import java.util.Date;

/**
 * 流程定义视图对象 workflow_definition
 *
 * @author hwb
 * @date 2022/04/30 20:35
 */
@Data
public class WorkflowDefinitionDTO {

    /**
     * 流程定义ID
     */
    private String definitionId;

    /**
     * 流程名称
     */
    private String processName;

    /**
     * 流程Key
     */
    private String processKey;

    /**
     * 分类编码
     */
    private String category;

    /**
     * 版本
     */
    private Integer version;

    /**
     * 表单ID
     */
    private Long formId;

    /**
     * 表单名称
     */
    private String formName;

    /**
     * 部署ID
     */
    private String deploymentId;

    /**
     * 流程定义状态: 1:激活 , 2:中止
     */
    private Boolean suspended;

    /**
     * 部署时间
     */
    private Date deploymentTime;

}
