package com.hwenbin.server.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.BetweenFormatter;
import cn.hutool.core.date.DateUtil;
import com.google.common.collect.Lists;
import com.hwenbin.server.core.constant.TaskConstant;
import com.hwenbin.server.core.exception.ServiceException;
import com.hwenbin.server.core.flowable.factory.FlowServiceFactory;
import com.hwenbin.server.core.web.response.PageParam;
import com.hwenbin.server.core.web.response.PageResult;
import com.hwenbin.server.dto.flow.WorkflowDefinitionDTO;
import com.hwenbin.server.dto.flow.WorkflowTaskDTO;
import com.hwenbin.server.entity.Employee;
import com.hwenbin.server.service.DepartmentService;
import com.hwenbin.server.service.EmployeeService;
import com.hwenbin.server.service.WorkflowProcessService;
import com.hwenbin.server.service.WorkflowTaskService;
import com.hwenbin.server.util.ContextUtils;
import com.hwenbin.server.util.DateUtils;
import com.hwenbin.server.util.StringUtils;
import com.hwenbin.server.util.flow.TaskUtils;
import org.flowable.engine.history.HistoricProcessInstance;
import org.flowable.engine.history.HistoricProcessInstanceQuery;
import org.flowable.engine.repository.Deployment;
import org.flowable.engine.repository.ProcessDefinition;
import org.flowable.engine.repository.ProcessDefinitionQuery;
import org.flowable.engine.runtime.ProcessInstance;
import org.flowable.task.api.Task;
import org.flowable.task.api.TaskQuery;
import org.flowable.task.api.history.HistoricTaskInstance;
import org.flowable.task.api.history.HistoricTaskInstanceQuery;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * @author hwb
 * @date 2022/05/01 15:57
 */
@Service
public class WorkflowProcessServiceImpl extends FlowServiceFactory implements WorkflowProcessService {

    @Resource
    private WorkflowTaskService wfTaskService;

    @Resource
    private EmployeeService employeeService;

    @Resource
    private DepartmentService departmentService;

    @Override
    public PageResult<WorkflowDefinitionDTO> processList(PageParam pageParam) {
        PageResult<WorkflowDefinitionDTO> page = new PageResult<>();
        // 流程定义列表数据查询
        ProcessDefinitionQuery processDefinitionQuery = repositoryService.createProcessDefinitionQuery()
                .latestVersion()
                .active()
                .orderByProcessDefinitionKey()
                .asc();
        long pageTotal = processDefinitionQuery.count();
        if (pageTotal <= 0) {
            return PageResult.empty();
        }
        int offset = pageParam.getPageSize() * (pageParam.getPageNo() - 1);
        List<ProcessDefinition> definitionList = processDefinitionQuery.listPage(offset, pageParam.getPageSize());

        List<WorkflowDefinitionDTO> definitionVoList = new ArrayList<>();
        for (ProcessDefinition processDefinition : definitionList) {
            String deploymentId = processDefinition.getDeploymentId();
            Deployment deployment = repositoryService.createDeploymentQuery().deploymentId(deploymentId).singleResult();
            WorkflowDefinitionDTO dto = new WorkflowDefinitionDTO();
            dto.setDefinitionId(processDefinition.getId());
            dto.setProcessKey(processDefinition.getKey());
            dto.setProcessName(processDefinition.getName());
            dto.setVersion(processDefinition.getVersion());
            dto.setCategory(processDefinition.getCategory());
            dto.setDeploymentId(processDefinition.getDeploymentId());
            dto.setSuspended(processDefinition.isSuspended());
            // 流程定义时间
            dto.setCategory(deployment.getCategory());
            dto.setDeploymentTime(deployment.getDeploymentTime());
            definitionVoList.add(dto);
        }
        page.setList(definitionVoList);
        page.setTotal(pageTotal);
        return page;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void startProcess(String procDefId, Map<String, Object> variables) {
        try {
            ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery()
                    .processDefinitionId(procDefId).singleResult();
            if (Objects.nonNull(processDefinition) && processDefinition.isSuspended()) {
                throw new ServiceException("流程已被挂起，请先激活流程");
            }
            // 设置流程发起人Id到流程中
            this.buildProcessVariables(variables);
            ProcessInstance processInstance = runtimeService.startProcessInstanceById(procDefId, variables);
            // 给第一步申请人节点设置任务执行人和意见 todo:第一个节点不设置为申请人节点有点问题？
            wfTaskService.startFirstTask(processInstance, variables);
        } catch (Exception e) {
            e.printStackTrace();
            throw new ServiceException("流程启动错误");
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void startProcessByDefKey(String procDefKey, Map<String, Object> variables) {
        try {
            if (StringUtils.isNoneBlank(procDefKey)) {
                ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery()
                        .processDefinitionKey(procDefKey).latestVersion().singleResult();
                if (processDefinition != null && processDefinition.isSuspended()) {
                    throw new ServiceException("流程已被挂起，请先激活流程");
                }
                this.buildProcessVariables(variables);
                ProcessInstance processInstance = runtimeService.startProcessInstanceByKey(procDefKey, variables);
                wfTaskService.startFirstTask(processInstance, variables);
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new ServiceException("流程启动错误");
        }
    }

    @Override
    public PageResult<WorkflowTaskDTO> queryPageOwnProcessList(PageParam pageParam) {
        PageResult<WorkflowTaskDTO> page = new PageResult<>();
        Long userId = ContextUtils.getCustomerUserDetails().getId();
        HistoricProcessInstanceQuery historicProcessInstanceQuery = historyService.createHistoricProcessInstanceQuery()
                .startedBy(userId.toString())
                .orderByProcessInstanceStartTime()
                .desc();
        int offset = pageParam.getPageSize() * (pageParam.getPageNo() - 1);
        List<HistoricProcessInstance> historicProcessInstances = historicProcessInstanceQuery
                .listPage(offset, pageParam.getPageSize());
        page.setTotal(historicProcessInstanceQuery.count());
        List<WorkflowTaskDTO> taskVoList = new ArrayList<>();
        for (HistoricProcessInstance hisIns : historicProcessInstances) {
            WorkflowTaskDTO taskDTO = new WorkflowTaskDTO();
            taskDTO.setCreateTime(hisIns.getStartTime());
            taskDTO.setFinishTime(hisIns.getEndTime());
            taskDTO.setProcInsId(hisIns.getId());

            // 计算耗时
            if (Objects.nonNull(hisIns.getEndTime())) {
                taskDTO.setDuration(DateUtils.getDatePoor(hisIns.getEndTime(), hisIns.getStartTime()));
            } else {
                taskDTO.setDuration(DateUtils.getDatePoor(DateUtils.getNowDate(), hisIns.getStartTime()));
            }
            // 流程部署实例信息
            Deployment deployment = repositoryService.createDeploymentQuery()
                    .deploymentId(hisIns.getDeploymentId()).singleResult();
            taskDTO.setDeployId(hisIns.getDeploymentId());
            taskDTO.setProcDefId(hisIns.getProcessDefinitionId());
            taskDTO.setProcDefName(hisIns.getProcessDefinitionName());
            taskDTO.setProcDefVersion(hisIns.getProcessDefinitionVersion());
            taskDTO.setCategory(deployment.getCategory());
            // 当前所处流程
            List<Task> taskList = taskService.createTaskQuery().processInstanceId(hisIns.getId()).list();
            if (CollUtil.isNotEmpty(taskList)) {
                taskDTO.setTaskId(taskList.get(0).getId());
            } else {
                List<HistoricTaskInstance> historicTaskInstance = historyService.createHistoricTaskInstanceQuery()
                        .processInstanceId(hisIns.getId()).orderByHistoricTaskInstanceEndTime().desc().list();
                taskDTO.setTaskId(historicTaskInstance.get(0).getId());
            }
            taskVoList.add(taskDTO);
        }
        page.setList(taskVoList);
        return page;
    }

    @Override
    public PageResult<WorkflowTaskDTO> queryPageTodoProcessList(PageParam pageParam) {
        PageResult<WorkflowTaskDTO> page = new PageResult<>();
        Long userId = ContextUtils.getCustomerUserDetails().getId();
        TaskQuery taskQuery = taskService.createTaskQuery()
                .active()
                .includeProcessVariables()
                .taskCandidateOrAssigned(userId.toString())
                .taskCandidateGroupIn(TaskUtils.getCandidateGroup())
                .orderByTaskCreateTime().desc();
        page.setTotal(taskQuery.count());
        int offset = pageParam.getPageSize() * (pageParam.getPageNo() - 1);
        List<Task> taskList = taskQuery.listPage(offset, pageParam.getPageSize());
        List<WorkflowTaskDTO> flowList = new ArrayList<>();
        for (Task task : taskList) {
            WorkflowTaskDTO flowTask = new WorkflowTaskDTO();
            // 当前流程信息
            flowTask.setTaskId(task.getId());
            flowTask.setTaskDefKey(task.getTaskDefinitionKey());
            flowTask.setCreateTime(task.getCreateTime());
            flowTask.setProcDefId(task.getProcessDefinitionId());
            flowTask.setTaskName(task.getName());
            // 流程定义信息
            ProcessDefinition pd = repositoryService.createProcessDefinitionQuery()
                    .processDefinitionId(task.getProcessDefinitionId())
                    .singleResult();
            flowTask.setDeployId(pd.getDeploymentId());
            flowTask.setProcDefName(pd.getName());
            flowTask.setProcDefVersion(pd.getVersion());
            flowTask.setProcInsId(task.getProcessInstanceId());

            // 流程发起人信息
            HistoricProcessInstance historicProcessInstance = historyService.createHistoricProcessInstanceQuery()
                    .processInstanceId(task.getProcessInstanceId())
                    .singleResult();
            Employee startUser = employeeService.getEmployeeById(Long.parseLong(historicProcessInstance.getStartUserId()));
            flowTask.setStartUserId(startUser.getName());
            flowTask.setStartUserName(startUser.getName());
            flowTask.setStartDeptName(departmentService.getById(startUser.getDeptId()).getName());

            // 流程变量
            flowTask.setProcVars(this.getProcessVariables(task.getId()));

            flowList.add(flowTask);
        }
        page.setList(flowList);
        return page;
    }

    @Override
    public PageResult<WorkflowTaskDTO> queryPageFinishedProcessList(PageParam pageParam) {
        PageResult<WorkflowTaskDTO> page = new PageResult<>();
        Long userId = ContextUtils.getCustomerUserDetails().getId();
        HistoricTaskInstanceQuery taskInstanceQuery = historyService.createHistoricTaskInstanceQuery()
                .includeProcessVariables()
                .finished()
                .taskAssignee(userId.toString())
                .orderByHistoricTaskInstanceEndTime()
                .desc();
        int offset = pageParam.getPageSize() * (pageParam.getPageNo() - 1);
        List<HistoricTaskInstance> historicTaskInstanceList = taskInstanceQuery.listPage(offset, pageParam.getPageSize());
        List<WorkflowTaskDTO> hisTaskList = Lists.newArrayList();
        for (HistoricTaskInstance histTask : historicTaskInstanceList) {
            WorkflowTaskDTO flowTask = new WorkflowTaskDTO();
            // 当前流程信息
            flowTask.setTaskId(histTask.getId());
            // 审批人员信息
            flowTask.setCreateTime(histTask.getCreateTime());
            flowTask.setFinishTime(histTask.getEndTime());
            flowTask.setDuration(DateUtil.formatBetween(histTask.getDurationInMillis(), BetweenFormatter.Level.SECOND));
            flowTask.setProcDefId(histTask.getProcessDefinitionId());
            flowTask.setTaskDefKey(histTask.getTaskDefinitionKey());
            flowTask.setTaskName(histTask.getName());

            // 流程定义信息
            ProcessDefinition pd = repositoryService.createProcessDefinitionQuery()
                    .processDefinitionId(histTask.getProcessDefinitionId())
                    .singleResult();
            flowTask.setDeployId(pd.getDeploymentId());
            flowTask.setProcDefName(pd.getName());
            flowTask.setProcDefVersion(pd.getVersion());
            flowTask.setProcInsId(histTask.getProcessInstanceId());
            flowTask.setHisProcInsId(histTask.getProcessInstanceId());

            // 流程发起人信息
            HistoricProcessInstance historicProcessInstance = historyService.createHistoricProcessInstanceQuery()
                    .processInstanceId(histTask.getProcessInstanceId())
                    .singleResult();
            Employee startUser = employeeService.getEmployeeById(Long.parseLong(historicProcessInstance.getStartUserId()));
            flowTask.setStartUserId(startUser.getName());
            flowTask.setStartUserName(startUser.getName());
            flowTask.setStartDeptName(departmentService.getById(startUser.getDeptId()).getName());

            // 流程变量
            flowTask.setProcVars(this.getProcessVariables(histTask.getId()));

            hisTaskList.add(flowTask);
        }
        page.setTotal(taskInstanceQuery.count());
        page.setList(hisTaskList);
//        Map<String, Object> result = new HashMap<>();
//        result.put("result",page);
//        result.put("finished",true);
        return page;
    }

    /**
     * 扩展参数构建
     * @param variables 扩展参数
     */
    private void buildProcessVariables(Map<String, Object> variables) {
        String userIdStr = ContextUtils.getCustomerUserDetails().getId().toString();
        identityService.setAuthenticatedUserId(userIdStr);
        variables.put(TaskConstant.PROCESS_INITIATOR, userIdStr);
    }

    /**
     * 获取流程变量
     *
     * @param taskId 任务ID
     * @return 流程变量
     */
    private Map<String, Object> getProcessVariables(String taskId) {
        HistoricTaskInstance historicTaskInstance = historyService.createHistoricTaskInstanceQuery()
                .includeProcessVariables()
                .finished()
                .taskId(taskId)
                .singleResult();
        if (Objects.nonNull(historicTaskInstance)) {
            return historicTaskInstance.getProcessVariables();
        }
        return taskService.getVariables(taskId);
    }

}
