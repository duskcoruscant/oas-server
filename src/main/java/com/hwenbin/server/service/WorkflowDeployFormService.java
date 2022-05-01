package com.hwenbin.server.service;

import com.hwenbin.server.entity.WorkflowDeployFormEntity;
import com.hwenbin.server.entity.WorkflowFormEntity;

/**
 * 流程实例关联表单Service接口
 *
 * @author hwb
 * @date 2022/04/30 20:08
 */
public interface WorkflowDeployFormService {

    /**
     * 新增流程实例关联表单
     *
     * @param workflowDeployFormEntity 流程实例关联表单
     * @return 结果
     */
    Integer insertWorkflowDeployForm(WorkflowDeployFormEntity workflowDeployFormEntity);

    /**
     * 查询流程挂载的表单
     *
     * @param deployId 流程实例id
     * @return 表单信息
     */
    WorkflowFormEntity selectDeployFormByDeployId(String deployId);

}
