package com.hwenbin.server.service;

import com.hwenbin.server.entity.WorkflowTaskEntity;
import org.flowable.engine.history.HistoricProcessInstance;

import java.util.Map;

/**
 * @author hwb
 * @date 2022/05/02 21:10
 */
public interface WorkflowInstanceService {

    /**
     * 结束流程实例
     *
     * @param vo
     */
    void stopProcessInstance(WorkflowTaskEntity vo);

    /**
     * 激活或挂起流程实例
     *
     * @param state      状态
     * @param instanceId 流程实例ID
     */
    void updateState(Integer state, String instanceId);

    /**
     * 删除流程实例ID
     *
     * @param instanceId   流程实例ID
     * @param deleteReason 删除原因
     */
    void delete(String instanceId, String deleteReason);

    /**
     * 根据实例ID查询历史实例数据
     *
     * @param processInstanceId 实例ID
     * @return
     */
    HistoricProcessInstance getHistoricProcessInstanceById(String processInstanceId);


    /**
     * 查询流程详情信息
     * @param procInsId 流程实例ID
     * @param deployId 流程部署ID
     */
    Map<String, Object> queryDetailProcess(String procInsId, String deployId);

}
