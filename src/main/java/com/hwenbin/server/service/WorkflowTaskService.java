package com.hwenbin.server.service;

import com.hwenbin.server.core.web.response.PageParam;
import com.hwenbin.server.core.web.response.PageResult;
import com.hwenbin.server.dto.flow.WorkflowNextDTO;
import com.hwenbin.server.dto.flow.WorkflowTaskDTO;
import com.hwenbin.server.dto.flow.WorkflowViewerDTO;
import com.hwenbin.server.entity.WorkflowTaskEntity;
import org.flowable.bpmn.model.UserTask;
import org.flowable.engine.runtime.ProcessInstance;

import java.io.InputStream;
import java.util.List;
import java.util.Map;

/**
 * @author hwb
 * @date 2022/05/01 15:59
 */
public interface WorkflowTaskService {

    /**
     * 审批任务
     *
     * @param taskEntity 请求实体参数
     */
    void complete(WorkflowTaskEntity taskEntity);

    /**
     * 驳回任务
     *
     * @param taskEntity 请求实体参数
     */
    void taskReject(WorkflowTaskEntity taskEntity);


    /**
     * 退回任务
     *
     * @param taskEntity 请求实体参数
     */
    void taskReturn(WorkflowTaskEntity taskEntity);

    /**
     * 获取所有可回退的节点
     *
     * @param taskEntity 请求实体参数
     * @return
     */
    List<UserTask> findReturnTaskList(WorkflowTaskEntity taskEntity);

    /**
     * 删除任务
     *
     * @param taskEntity 请求实体参数
     */
    void deleteTask(WorkflowTaskEntity taskEntity);

    /**
     * 认领/签收任务
     *
     * @param taskEntity 请求实体参数
     */
    void claim(WorkflowTaskEntity taskEntity);

    /**
     * 取消认领/签收任务
     *
     * @param taskEntity 请求实体参数
     */
    void unClaim(WorkflowTaskEntity taskEntity);

    /**
     * 委派任务
     *
     * @param taskEntity 请求实体参数
     */
    void delegateTask(WorkflowTaskEntity taskEntity);


    /**
     * 转办任务
     *
     * @param taskEntity 请求实体参数
     */
    void transferTask(WorkflowTaskEntity taskEntity);

    /**
     * 取消申请
     * @param taskEntity 请求实体参数
     * @return
     */
    void stopProcess(WorkflowTaskEntity taskEntity);

    /**
     * 撤回流程
     * @param taskEntity 请求实体参数
     * @return
     */
    void revokeProcess(WorkflowTaskEntity taskEntity);


    /**
     * 代办任务列表
     *
     * @return
     */
    PageResult<WorkflowTaskDTO> todoList(PageParam pageParam);


    /**
     * 已办任务列表
     *
     * @return
     */
    PageResult<WorkflowTaskDTO> finishedList(PageParam pageParam);

    /**
     * 获取流程过程图
     * @param processId
     * @return
     */
    InputStream diagram(String processId);

    /**
     * 获取流程执行过程
     * @param procInsId
     * @return
     */
    WorkflowViewerDTO getFlowViewer(String procInsId);

    /**
     * 获取流程变量
     * @param taskId 任务ID
     * @return 流程变量
     */
    Map<String, Object> getProcessVariables(String taskId);

    /**
     * 获取下一节点
     * @param taskEntity 任务
     * @return
     */
    WorkflowNextDTO getNextFlowNode(WorkflowTaskEntity taskEntity);

    /**
     * 启动第一个任务
     * @param processInstance 流程实例
     * @param variables 流程参数
     */
    void startFirstTask(ProcessInstance processInstance, Map<String, Object> variables);

}
