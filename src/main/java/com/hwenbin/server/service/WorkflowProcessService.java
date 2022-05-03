package com.hwenbin.server.service;

import com.hwenbin.server.core.web.response.PageParam;
import com.hwenbin.server.core.web.response.PageResult;
import com.hwenbin.server.dto.flow.WorkflowDefinitionDTO;
import com.hwenbin.server.dto.flow.WorkflowTaskDTO;

import java.util.Map;

/**
 * @author hwb
 * @date 2022/05/01 15:45
 */
public interface WorkflowProcessService {

    /**
     * 分页查询可发起流程列表
     * @param pageParam 分页参数
     * @return 流程定义分页列表数据
     */
    PageResult<WorkflowDefinitionDTO> processList(PageParam pageParam);

    /**
     * 根据流程定义ID启动流程实例
     * @param procDefId 流程定义ID
     * @param variables 流程变量
     */
    void startProcess(String procDefId, Map<String, Object> variables);

    /**
     * 通过DefinitionKey启动流程
     * @param procDefKey 流程定义Key
     * @param variables 扩展参数
     */
    void startProcessByDefKey(String procDefKey, Map<String, Object> variables);

    /**
     * 分页查询我的流程列表
     * @param pageParam 分页参数
     */
    PageResult<WorkflowTaskDTO> queryPageOwnProcessList(PageParam pageParam);

    /**
     * 分页查询代办任务列表
     * @param pageParam 分页参数
     */
    PageResult<WorkflowTaskDTO> queryPageTodoProcessList(PageParam pageParam);

    /**
     * 分页查询已办任务列表
     * @param pageParam 分页参数
     */
    PageResult<WorkflowTaskDTO> queryPageFinishedProcessList(PageParam pageParam);

}
