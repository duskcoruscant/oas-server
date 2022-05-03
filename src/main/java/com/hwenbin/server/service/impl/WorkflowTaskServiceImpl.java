package com.hwenbin.server.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.google.common.collect.Lists;
import com.hwenbin.server.core.constant.ProcessConstant;
import com.hwenbin.server.core.constant.TaskConstant;
import com.hwenbin.server.core.exception.ServiceException;
import com.hwenbin.server.core.flowable.factory.FlowServiceFactory;
import com.hwenbin.server.core.flowable.flow.CustomProcessDiagramGenerator;
import com.hwenbin.server.core.web.response.PageParam;
import com.hwenbin.server.core.web.response.PageResult;
import com.hwenbin.server.dto.CustomerUserDetails;
import com.hwenbin.server.dto.flow.WorkflowNextDTO;
import com.hwenbin.server.dto.flow.WorkflowTaskDTO;
import com.hwenbin.server.dto.flow.WorkflowViewerDTO;
import com.hwenbin.server.entity.Employee;
import com.hwenbin.server.entity.Role;
import com.hwenbin.server.entity.WorkflowTaskEntity;
import com.hwenbin.server.enums.FlowCommentEnum;
import com.hwenbin.server.service.DepartmentService;
import com.hwenbin.server.service.EmployeeService;
import com.hwenbin.server.service.RoleService;
import com.hwenbin.server.service.WorkflowTaskService;
import com.hwenbin.server.util.ContextUtils;
import com.hwenbin.server.util.StringUtils;
import com.hwenbin.server.util.flow.FindNextNodeUtil;
import com.hwenbin.server.util.flow.FlowableUtils;
import lombok.extern.slf4j.Slf4j;
import org.flowable.bpmn.model.Process;
import org.flowable.bpmn.model.*;
import org.flowable.common.engine.api.FlowableException;
import org.flowable.common.engine.api.FlowableObjectNotFoundException;
import org.flowable.common.engine.impl.identity.Authentication;
import org.flowable.engine.ProcessEngineConfiguration;
import org.flowable.engine.history.HistoricActivityInstance;
import org.flowable.engine.history.HistoricActivityInstanceQuery;
import org.flowable.engine.history.HistoricProcessInstance;
import org.flowable.engine.repository.ProcessDefinition;
import org.flowable.engine.runtime.Execution;
import org.flowable.engine.runtime.ProcessInstance;
import org.flowable.image.ProcessDiagramGenerator;
import org.flowable.task.api.DelegationState;
import org.flowable.task.api.Task;
import org.flowable.task.api.TaskQuery;
import org.flowable.task.api.history.HistoricTaskInstance;
import org.flowable.task.api.history.HistoricTaskInstanceQuery;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.io.InputStream;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author hwb
 * @date 2022/05/01 16:12
 */
@Slf4j
@Service
public class WorkflowTaskServiceImpl extends FlowServiceFactory implements WorkflowTaskService {

    @Resource
    private EmployeeService employeeService;

    @Resource
    private RoleService roleService;

    @Resource
    private DepartmentService departmentService;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void complete(WorkflowTaskEntity taskEntity) {
        Task task = taskService.createTaskQuery().taskId(taskEntity.getTaskId()).singleResult();
        if (Objects.isNull(task)) {
            throw new ServiceException("任务不存在");
        }
        if (DelegationState.PENDING.equals(task.getDelegationState())) {
            taskService.addComment(taskEntity.getTaskId(), taskEntity.getInstanceId(), FlowCommentEnum.DELEGATE.getType(), taskEntity.getComment());
            taskService.resolveTask(taskEntity.getTaskId(), taskEntity.getValues());
        } else {
            taskService.addComment(taskEntity.getTaskId(), taskEntity.getInstanceId(), FlowCommentEnum.NORMAL.getType(), taskEntity.getComment());
            Long userId = ContextUtils.getCustomerUserDetails().getId();
            taskService.setAssignee(taskEntity.getTaskId(), userId.toString());
            if (ObjectUtil.isNotEmpty(taskEntity.getValues())) {
                taskService.complete(taskEntity.getTaskId(), taskEntity.getValues());
            } else {
                taskService.complete(taskEntity.getTaskId());
            }
        }
    }

    @Override
    public void taskReject(WorkflowTaskEntity taskEntity) {
        // 当前任务 task
        Task task = taskService.createTaskQuery().taskId(taskEntity.getTaskId()).singleResult();
        if (ObjectUtil.isNull(task)) {
            throw new ServiceException("获取任务信息异常！");
        }
        if (task.isSuspended()) {
            throw new ServiceException("任务处于挂起状态");
        }
        // 获取流程定义信息
        ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery().processDefinitionId(task.getProcessDefinitionId()).singleResult();
        // 获取所有节点信息
        Process process = repositoryService.getBpmnModel(processDefinition.getId()).getProcesses().get(0);
        // 获取全部节点列表，包含子节点
        Collection<FlowElement> allElements = FlowableUtils.getAllElements(process.getFlowElements(), null);
        // 获取当前任务节点元素
        FlowElement source = null;
        if (allElements != null) {
            for (FlowElement flowElement : allElements) {
                // 类型为用户节点
                if (flowElement.getId().equals(task.getTaskDefinitionKey())) {
                    // 获取节点信息
                    source = flowElement;
                }
            }
        }

        // 目的获取所有跳转到的节点 targetIds
        // 获取当前节点的所有父级用户任务节点
        // 深度优先算法思想：延边迭代深入
        List<UserTask> parentUserTaskList = FlowableUtils.iteratorFindParentUserTasks(source, null, null);
        if (parentUserTaskList == null || parentUserTaskList.size() == 0) {
            throw new ServiceException("当前节点为初始任务节点，不能驳回");
        }
        // 获取活动 ID 即节点 Key
        List<String> parentUserTaskKeyList = new ArrayList<>();
        parentUserTaskList.forEach(item -> parentUserTaskKeyList.add(item.getId()));
        // 获取全部历史节点活动实例，即已经走过的节点历史，数据采用开始时间升序
        List<HistoricTaskInstance> historicTaskInstanceList = historyService.createHistoricTaskInstanceQuery().processInstanceId(task.getProcessInstanceId()).orderByHistoricTaskInstanceStartTime().asc().list();
        // 数据清洗，将回滚导致的脏数据清洗掉
        List<String> lastHistoricTaskInstanceList = FlowableUtils.historicTaskInstanceClean(allElements, historicTaskInstanceList);
        // 此时历史任务实例为倒序，获取最后走的节点
        List<String> targetIds = new ArrayList<>();
        // 循环结束标识，遇到当前目标节点的次数
        int number = 0;
        StringBuilder parentHistoricTaskKey = new StringBuilder();
        for (String historicTaskInstanceKey : lastHistoricTaskInstanceList) {
            // 当会签时候会出现特殊的，连续都是同一个节点历史数据的情况，这种时候跳过
            if (parentHistoricTaskKey.toString().equals(historicTaskInstanceKey)) {
                continue;
            }
            parentHistoricTaskKey = new StringBuilder(historicTaskInstanceKey);
            if (historicTaskInstanceKey.equals(task.getTaskDefinitionKey())) {
                number++;
            }
            // 在数据清洗后，历史节点就是唯一一条从起始到当前节点的历史记录，理论上每个点只会出现一次
            // 在流程中如果出现循环，那么每次循环中间的点也只会出现一次，再出现就是下次循环
            // number == 1，第一次遇到当前节点
            // number == 2，第二次遇到，代表最后一次的循环范围
            if (number == 2) {
                break;
            }
            // 如果当前历史节点，属于父级的节点，说明最后一次经过了这个点，需要退回这个点
            if (parentUserTaskKeyList.contains(historicTaskInstanceKey)) {
                targetIds.add(historicTaskInstanceKey);
            }
        }


        // 目的获取所有需要被跳转的节点 currentIds
        // 取其中一个父级任务，因为后续要么存在公共网关，要么就是串行公共线路
        UserTask oneUserTask = parentUserTaskList.get(0);
        // 获取所有正常进行的任务节点 Key，这些任务不能直接使用，需要找出其中需要撤回的任务
        List<Task> runTaskList = taskService.createTaskQuery().processInstanceId(task.getProcessInstanceId()).list();
        List<String> runTaskKeyList = new ArrayList<>();
        runTaskList.forEach(item -> runTaskKeyList.add(item.getTaskDefinitionKey()));
        // 需驳回任务列表
        List<String> currentIds = new ArrayList<>();
        // 通过父级网关的出口连线，结合 runTaskList 比对，获取需要撤回的任务
        List<UserTask> currentUserTaskList = FlowableUtils.iteratorFindChildUserTasks(oneUserTask, runTaskKeyList, null, null);
        currentUserTaskList.forEach(item -> currentIds.add(item.getId()));


        // 规定：并行网关之前节点必须需存在唯一用户任务节点，如果出现多个任务节点，则并行网关节点默认为结束节点，原因为不考虑多对多情况
        if (targetIds.size() > 1 && currentIds.size() > 1) {
            throw new ServiceException("任务出现多对多情况，无法撤回");
        }

        // 循环获取那些需要被撤回的节点的ID，用来设置驳回原因
        List<String> currentTaskIds = new ArrayList<>();
        currentIds.forEach(currentId -> runTaskList.forEach(runTask -> {
            if (currentId.equals(runTask.getTaskDefinitionKey())) {
                currentTaskIds.add(runTask.getId());
            }
        }));
        // 设置驳回意见
        currentTaskIds.forEach(item -> taskService.addComment(item, task.getProcessInstanceId(), FlowCommentEnum.REJECT.getType(), taskEntity.getComment()));

        try {
            // 如果父级任务多于 1 个，说明当前节点不是并行节点，原因为不考虑多对多情况
            if (targetIds.size() > 1) {
                // 1 对 多任务跳转，currentIds 当前节点(1)，targetIds 跳转到的节点(多)
                runtimeService.createChangeActivityStateBuilder()
                        .processInstanceId(task.getProcessInstanceId()).
                        moveSingleActivityIdToActivityIds(currentIds.get(0), targetIds).changeState();
            }
            // 如果父级任务只有一个，因此当前任务可能为网关中的任务
            if (targetIds.size() == 1) {
                // 1 对 1 或 多 对 1 情况，currentIds 当前要跳转的节点列表(1或多)，targetIds.get(0) 跳转到的节点(1)
                runtimeService.createChangeActivityStateBuilder()
                        .processInstanceId(task.getProcessInstanceId())
                        .moveActivityIdsToSingleActivityId(currentIds, targetIds.get(0)).changeState();
            }
        } catch (FlowableObjectNotFoundException e) {
            throw new ServiceException("未找到流程实例，流程可能已发生变化");
        } catch (FlowableException e) {
            throw new ServiceException("无法取消或开始活动");
        }

    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void taskReturn(WorkflowTaskEntity taskEntity) {
        // 当前任务 task
        Task task = taskService.createTaskQuery().taskId(taskEntity.getTaskId()).singleResult();
        if (ObjectUtil.isNull(task)) {
            throw new ServiceException("获取任务信息异常！");
        }
        if (task.isSuspended()) {
            throw new ServiceException("任务处于挂起状态");
        }
        // 获取流程定义信息
        ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery().processDefinitionId(task.getProcessDefinitionId()).singleResult();
        // 获取所有节点信息
        Process process = repositoryService.getBpmnModel(processDefinition.getId()).getProcesses().get(0);
        // 获取全部节点列表，包含子节点
        Collection<FlowElement> allElements = FlowableUtils.getAllElements(process.getFlowElements(), null);
        // 获取当前任务节点元素
        FlowElement source = null;
        // 获取跳转的节点元素
        FlowElement target = null;
        if (allElements != null) {
            for (FlowElement flowElement : allElements) {
                // 当前任务节点元素
                if (flowElement.getId().equals(task.getTaskDefinitionKey())) {
                    source = flowElement;
                }
                // 跳转的节点元素
                if (flowElement.getId().equals(taskEntity.getTargetKey())) {
                    target = flowElement;
                }
            }
        }

        // 从当前节点向前扫描
        // 如果存在路线上不存在目标节点，说明目标节点是在网关上或非同一路线上，不可跳转
        // 否则目标节点相对于当前节点，属于串行
        Boolean isSequential = FlowableUtils.iteratorCheckSequentialReferTarget(source, taskEntity.getTargetKey(), null, null);
        if (!isSequential) {
            throw new ServiceException("当前节点相对于目标节点，不属于串行关系，无法回退");
        }


        // 获取所有正常进行的任务节点 Key，这些任务不能直接使用，需要找出其中需要撤回的任务
        List<Task> runTaskList = taskService.createTaskQuery().processInstanceId(task.getProcessInstanceId()).list();
        List<String> runTaskKeyList = new ArrayList<>();
        runTaskList.forEach(item -> runTaskKeyList.add(item.getTaskDefinitionKey()));
        // 需退回任务列表
        List<String> currentIds = new ArrayList<>();
        // 通过父级网关的出口连线，结合 runTaskList 比对，获取需要撤回的任务
        List<UserTask> currentUserTaskList = FlowableUtils.iteratorFindChildUserTasks(target, runTaskKeyList, null, null);
        currentUserTaskList.forEach(item -> currentIds.add(item.getId()));

        // 循环获取那些需要被撤回的节点的ID，用来设置驳回原因
        List<String> currentTaskIds = new ArrayList<>();
        currentIds.forEach(currentId -> runTaskList.forEach(runTask -> {
            if (currentId.equals(runTask.getTaskDefinitionKey())) {
                currentTaskIds.add(runTask.getId());
            }
        }));
        // 设置回退意见
        for (String currentTaskId : currentTaskIds) {
            taskService.addComment(currentTaskId, task.getProcessInstanceId(), FlowCommentEnum.REBACK.getType(), taskEntity.getComment());
        }

        try {
            // 1 对 1 或 多 对 1 情况，currentIds 当前要跳转的节点列表(1或多)，targetKey 跳转到的节点(1)
            runtimeService.createChangeActivityStateBuilder()
                    .processInstanceId(task.getProcessInstanceId())
                    .moveActivityIdsToSingleActivityId(currentIds, taskEntity.getTargetKey()).changeState();
        } catch (FlowableObjectNotFoundException e) {
            throw new ServiceException("未找到流程实例，流程可能已发生变化");
        } catch (FlowableException e) {
            throw new ServiceException("无法取消或开始活动");
        }
    }

    @Override
    public List<UserTask> findReturnTaskList(WorkflowTaskEntity taskEntity) {
        // 当前任务 task
        Task task = taskService.createTaskQuery().taskId(taskEntity.getTaskId()).singleResult();
        // 获取流程定义信息
        ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery().processDefinitionId(task.getProcessDefinitionId()).singleResult();
        // 获取所有节点信息，暂不考虑子流程情况
        Process process = repositoryService.getBpmnModel(processDefinition.getId()).getProcesses().get(0);
        Collection<FlowElement> flowElements = process.getFlowElements();
        // 获取当前任务节点元素
        UserTask source = null;
        if (flowElements != null) {
            for (FlowElement flowElement : flowElements) {
                // 类型为用户节点
                if (flowElement.getId().equals(task.getTaskDefinitionKey())) {
                    source = (UserTask) flowElement;
                }
            }
        }
        // 获取节点的所有路线
        List<List<UserTask>> roads = FlowableUtils.findRoad(source, null, null, null);
        // 可回退的节点列表
        List<UserTask> userTaskList = new ArrayList<>();
        for (List<UserTask> road : roads) {
            if (userTaskList.size() == 0) {
                // 还没有可回退节点直接添加
                userTaskList = road;
            } else {
                // 如果已有回退节点，则比对取交集部分
                userTaskList.retainAll(road);
            }
        }
        return userTaskList;
    }

    @Override
    public void deleteTask(WorkflowTaskEntity taskEntity) {
        // todo 待确认删除任务是物理删除任务 还是逻辑删除，让这个任务直接通过？
        taskService.deleteTask(taskEntity.getTaskId(), taskEntity.getComment());
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void claim(WorkflowTaskEntity taskEntity) {
        taskService.claim(taskEntity.getTaskId(), taskEntity.getUserId());
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void unClaim(WorkflowTaskEntity taskEntity) {
        taskService.unclaim(taskEntity.getTaskId());
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void delegateTask(WorkflowTaskEntity taskEntity) {
        // 当前任务 task
        Task task = taskService.createTaskQuery().taskId(taskEntity.getTaskId()).singleResult();
        if (ObjectUtil.isEmpty(task)) {
            throw new ServiceException("获取任务失败！");
        }
        CustomerUserDetails loginUser = ContextUtils.getCustomerUserDetails();
        StringBuilder commentBuilder = new StringBuilder(loginUser.getUsername())
                .append("->");
        Employee user = employeeService.getEmployeeById(Long.parseLong(taskEntity.getUserId()));
        if (ObjectUtil.isNotNull(user)) {
            commentBuilder.append(user.getName());
        } else {
            commentBuilder.append(taskEntity.getUserId());
        }
        if (StringUtils.isNotBlank(taskEntity.getComment())) {
            commentBuilder.append(": ").append(taskEntity.getComment());
        }
        // 添加审批意见
        taskService.addComment(taskEntity.getTaskId(), task.getProcessInstanceId(), FlowCommentEnum.DELEGATE.getType(), commentBuilder.toString());
        // 设置办理人为当前登录人
        taskService.setOwner(taskEntity.getTaskId(), loginUser.getId().toString());
        // 执行委派
        taskService.delegateTask(taskEntity.getTaskId(), taskEntity.getUserId());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void transferTask(WorkflowTaskEntity taskEntity) {
        // 当前任务 task
        Task task = taskService.createTaskQuery().taskId(taskEntity.getTaskId()).singleResult();
        if (ObjectUtil.isEmpty(task)) {
            throw new ServiceException("获取任务失败！");
        }
        CustomerUserDetails loginUser = ContextUtils.getCustomerUserDetails();
        StringBuilder commentBuilder = new StringBuilder(loginUser.getUsername())
                .append("->");
        // SysUser user = sysUserService.selectUserById(Long.parseLong(taskEntity.getUserId()));
        Employee user = employeeService.getEmployeeById(Long.parseLong(taskEntity.getUserId()));
        if (ObjectUtil.isNotNull(user)) {
            commentBuilder.append(user.getName());
        } else {
            commentBuilder.append(taskEntity.getUserId());
        }
        if (StringUtils.isNotBlank(taskEntity.getComment())) {
            commentBuilder.append(": ").append(taskEntity.getComment());
        }
        // 添加审批意见
        taskService.addComment(taskEntity.getTaskId(), task.getProcessInstanceId(), FlowCommentEnum.TRANSFER.getType(), commentBuilder.toString());
        // 设置拥有者为当前登录人
        taskService.setOwner(taskEntity.getTaskId(), loginUser.getId().toString());
        // 转办任务
        taskService.setAssignee(taskEntity.getTaskId(), taskEntity.getUserId());
    }

    @Override
    public void stopProcess(WorkflowTaskEntity taskEntity) {
        List<Task> task = taskService.createTaskQuery().processInstanceId(taskEntity.getInstanceId()).list();
        if (CollUtil.isEmpty(task)) {
            throw new ServiceException("流程未启动或已执行完成，取消申请失败");
        }

        ProcessInstance processInstance = runtimeService.createProcessInstanceQuery()
                .processInstanceId(taskEntity.getInstanceId()).singleResult();
        BpmnModel bpmnModel = repositoryService.getBpmnModel(processInstance.getProcessDefinitionId());
        if (Objects.nonNull(bpmnModel)) {
            Process process = bpmnModel.getMainProcess();
            List<EndEvent> endNodes = process.findFlowElementsOfType(EndEvent.class, false);
            if (CollUtil.isNotEmpty(endNodes)) {
                Authentication.setAuthenticatedUserId(ContextUtils.getCustomerUserDetails().getId().toString());
//                taskService.addComment(task.getId(), processInstance.getProcessInstanceId(), FlowComment.STOP.getType(),
//                        StringUtils.isBlank(flowTaskVo.getComment()) ? "取消申请" : flowTaskVo.getComment());
                String endId = endNodes.get(0).getId();
                List<Execution> executions = runtimeService.createExecutionQuery()
                        .parentId(processInstance.getProcessInstanceId()).list();
                List<String> executionIds = new ArrayList<>();
                executions.forEach(execution -> executionIds.add(execution.getId()));
                runtimeService.createChangeActivityStateBuilder()
                        .moveExecutionsToSingleActivityId(executionIds, endId).changeState();
            }
        }
    }

    @Override
    public void revokeProcess(WorkflowTaskEntity taskEntity) {
        Task task = taskService.createTaskQuery().processInstanceId(taskEntity.getInstanceId()).singleResult();
        if (task == null) {
            throw new ServiceException("流程未启动或已执行完成，无法撤回");
        }

        List<HistoricTaskInstance> htiList = historyService.createHistoricTaskInstanceQuery()
                .processInstanceId(task.getProcessInstanceId())
                .orderByTaskCreateTime()
                .asc()
                .list();
        String myTaskId = null;
        HistoricTaskInstance myTask = null;
        for (HistoricTaskInstance hti : htiList) {
            if (ContextUtils.getCustomerUserDetails().getId().toString().equals(hti.getAssignee())) {
                myTaskId = hti.getId();
                myTask = hti;
                break;
            }
        }
        if (null == myTaskId) {
            throw new ServiceException("该任务非当前用户提交，无法撤回");
        }

        String processDefinitionId = myTask.getProcessDefinitionId();
        BpmnModel bpmnModel = repositoryService.getBpmnModel(processDefinitionId);

        //变量
//      Map<String, VariableInstance> variables = runtimeService.getVariableInstances(currentTask.getExecutionId());
        String myActivityId = null;
        List<HistoricActivityInstance> haiList = historyService.createHistoricActivityInstanceQuery()
                .executionId(myTask.getExecutionId()).finished().list();
        for (HistoricActivityInstance hai : haiList) {
            if (myTaskId.equals(hai.getTaskId())) {
                myActivityId = hai.getActivityId();
                break;
            }
        }
        FlowNode myFlowNode = (FlowNode) bpmnModel.getMainProcess().getFlowElement(myActivityId);

        Execution execution = runtimeService.createExecutionQuery().executionId(task.getExecutionId()).singleResult();
        String activityId = execution.getActivityId();
        FlowNode flowNode = (FlowNode) bpmnModel.getMainProcess().getFlowElement(activityId);

        //记录原活动方向
        List<SequenceFlow> oriSequenceFlows = new ArrayList<>(flowNode.getOutgoingFlows());
    }

    @Override
    public PageResult<WorkflowTaskDTO> todoList(PageParam pageParam) {
        PageResult<WorkflowTaskDTO> page = new PageResult<>();
        Long userId = ContextUtils.getCustomerUserDetails().getId();
        TaskQuery taskQuery = taskService.createTaskQuery()
                .active()
                .includeProcessVariables()
                .taskCandidateOrAssigned(userId.toString())
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
//            SysUser startUser = sysUserService.selectUserById(Long.parseLong(task.getAssignee()));
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
    public PageResult<WorkflowTaskDTO> finishedList(PageParam pageParam) {
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
            flowTask.setDuration(getDate(histTask.getDurationInMillis()));
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

    @Override
    public InputStream diagram(String processId) {
        String processDefinitionId;
        // 获取当前的流程实例
        ProcessInstance processInstance = runtimeService.createProcessInstanceQuery().processInstanceId(processId).singleResult();
        // 如果流程已经结束，则得到结束节点
        if (Objects.isNull(processInstance)) {
            HistoricProcessInstance pi = historyService.createHistoricProcessInstanceQuery().processInstanceId(processId).singleResult();

            processDefinitionId = pi.getProcessDefinitionId();
        } else {// 如果流程没有结束，则取当前活动节点
            // 根据流程实例ID获得当前处于活动状态的ActivityId合集
            ProcessInstance pi = runtimeService.createProcessInstanceQuery().processInstanceId(processId).singleResult();
            processDefinitionId = pi.getProcessDefinitionId();
        }

        // 获得活动的节点
        List<HistoricActivityInstance> highLightedFlowList = historyService.createHistoricActivityInstanceQuery()
                .processInstanceId(processId).orderByHistoricActivityInstanceStartTime().asc().list();

        List<String> highLightedFlows = new ArrayList<>();
        List<String> highLightedNodes = new ArrayList<>();
        //高亮线
        for (HistoricActivityInstance tempActivity : highLightedFlowList) {
            if ("sequenceFlow".equals(tempActivity.getActivityType())) {
                //高亮线
                highLightedFlows.add(tempActivity.getActivityId());
            } else {
                //高亮节点
                highLightedNodes.add(tempActivity.getActivityId());
            }
        }

        //获取流程图
        BpmnModel bpmnModel = repositoryService.getBpmnModel(processDefinitionId);
        ProcessEngineConfiguration configuration = processEngine.getProcessEngineConfiguration();
        //获取自定义图片生成器
        ProcessDiagramGenerator diagramGenerator = new CustomProcessDiagramGenerator();
        return diagramGenerator.generateDiagram(bpmnModel, "png", highLightedNodes, highLightedFlows, configuration.getActivityFontName(),
                configuration.getLabelFontName(), configuration.getAnnotationFontName(), configuration.getClassLoader(), 1.0, true);
    }

    @Override
    public WorkflowViewerDTO getFlowViewer(String procInsId) {
        // 构建查询条件
        HistoricActivityInstanceQuery query = historyService.createHistoricActivityInstanceQuery()
                .processInstanceId(procInsId);
        List<HistoricActivityInstance> allActivityInstanceList = query.list();
        if (CollUtil.isEmpty(allActivityInstanceList)) {
            return new WorkflowViewerDTO();
        }
        // 获取流程发布Id信息
        String processDefinitionId = allActivityInstanceList.get(0).getProcessDefinitionId();
        BpmnModel bpmnModel = repositoryService.getBpmnModel(processDefinitionId);
        // 查询所有已完成的元素
        List<HistoricActivityInstance> finishedElementList = allActivityInstanceList.stream()
                .filter(item -> ObjectUtil.isNotNull(item.getEndTime())).collect(Collectors.toList());
        // 所有已完成的连线
        Set<String> finishedSequenceFlowSet = new HashSet<>();
        // 所有已完成的任务节点
        Set<String> finishedTaskSet = new HashSet<>();
        finishedElementList.forEach(item -> {
            if ("sequenceFlow".equals(item.getActivityType())) {
                finishedSequenceFlowSet.add(item.getActivityId());
            } else {
                finishedTaskSet.add(item.getActivityId());
            }
        });
        // 查询所有未结束的节点
        Set<String> unfinishedTaskSet = allActivityInstanceList.stream()
                .filter(item -> ObjectUtil.isNull(item.getEndTime()))
                .map(HistoricActivityInstance::getActivityId)
                .collect(Collectors.toSet());
        // DFS 查询未通过的元素集合
        Set<String> rejectedSet = FlowableUtils.dfsFindRejects(bpmnModel, unfinishedTaskSet, finishedSequenceFlowSet, finishedTaskSet);
        return new WorkflowViewerDTO(finishedTaskSet, finishedSequenceFlowSet, unfinishedTaskSet, rejectedSet);
    }

    @Override
    public Map<String, Object> getProcessVariables(String taskId) {
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

    @Override
    public WorkflowNextDTO getNextFlowNode(WorkflowTaskEntity taskEntity) {
        Task task = taskService.createTaskQuery().taskId(taskEntity.getTaskId()).singleResult();
        WorkflowNextDTO nextDto = new WorkflowNextDTO();
        if (Objects.nonNull(task)) {
            List<UserTask> nextUserTask = FindNextNodeUtil.getNextUserTasks(repositoryService, task, new HashMap<>());
            if (CollUtil.isNotEmpty(nextUserTask)) {
                for (UserTask userTask : nextUserTask) {
                    MultiInstanceLoopCharacteristics multiInstance = userTask.getLoopCharacteristics();
                    // 会签节点
                    if (Objects.nonNull(multiInstance)) {
                        List<Employee> list = employeeService.list();

                        nextDto.setVars(ProcessConstant.PROCESS_MULTI_INSTANCE_USER);
                        nextDto.setType(ProcessConstant.PROCESS_MULTI_INSTANCE);
                        nextDto.setUserList(list);
                    } else {

                        // 读取自定义节点属性 判断是否是否需要动态指定任务接收人员、组
                        String dataType = userTask.getAttributeValue(ProcessConstant.NAMASPASE, ProcessConstant.PROCESS_CUSTOM_DATA_TYPE);
                        String userType = userTask.getAttributeValue(ProcessConstant.NAMASPASE, ProcessConstant.PROCESS_CUSTOM_USER_TYPE);

                        if (ProcessConstant.DATA_TYPE.equals(dataType)) {
                            // 指定单个人员
                            if (ProcessConstant.USER_TYPE_ASSIGNEE.equals(userType)) {
                                List<Employee> list = employeeService.list();

                                nextDto.setVars(ProcessConstant.PROCESS_APPROVAL);
                                nextDto.setType(ProcessConstant.USER_TYPE_ASSIGNEE);
                                nextDto.setUserList(list);
                            }
                            // 候选人员(多个)
                            if (ProcessConstant.USER_TYPE_USERS.equals(userType)) {
                                List<Employee> list = employeeService.list();

                                nextDto.setVars(ProcessConstant.PROCESS_APPROVAL);
                                nextDto.setType(ProcessConstant.USER_TYPE_USERS);
                                nextDto.setUserList(list);
                            }
                            // 候选组
                            if (ProcessConstant.USER_TYPE_ROUPS.equals(userType)) {
                                List<Role> sysRoles = roleService.list();

                                nextDto.setVars(ProcessConstant.PROCESS_APPROVAL);
                                nextDto.setType(ProcessConstant.USER_TYPE_ROUPS);
                                nextDto.setRoleList(sysRoles);
                            }
                        }
                    }
                }
            } else {
                return null;
            }
        }
        return nextDto;
    }

    @Override
    public void startFirstTask(ProcessInstance processInstance, Map<String, Object> variables) {
        // 给第一步申请人节点设置任务执行人和意见 todo:第一个节点不设置为申请人节点有点问题？
        Task task = taskService.createTaskQuery().processInstanceId(processInstance.getProcessInstanceId()).singleResult();
        if (Objects.nonNull(task)) {
            String userIdStr = (String) variables.get(TaskConstant.PROCESS_INITIATOR);
            if (!StrUtil.equalsAny(task.getAssignee(), userIdStr)) {
                throw new ServiceException("数据验证失败，该工作流第一个用户任务的指派人并非当前用户，不能执行该操作！");
            }
            taskService.addComment(task.getId(), processInstance.getProcessInstanceId(),
                    FlowCommentEnum.NORMAL.getType(), ContextUtils.getCustomerUserDetails().getUsername() + "发起流程申请");
            // taskService.setAssignee(task.getId(), userIdStr);
            taskService.complete(task.getId(), variables);
        }
    }

    /**
     * 流程完成时间处理
     *
     * @param ms 毫秒
     * @return 完成时间字符串
     */
    private String getDate(long ms) {

        long day = ms / (24 * 60 * 60 * 1000);
        long hour = (ms / (60 * 60 * 1000) - day * 24);
        long minute = ((ms / (60 * 1000)) - day * 24 * 60 - hour * 60);
        long second = (ms / 1000 - day * 24 * 60 * 60 - hour * 60 * 60 - minute * 60);

        if (day > 0) {
            return day + "天" + hour + "小时" + minute + "分钟";
        }
        if (hour > 0) {
            return hour + "小时" + minute + "分钟";
        }
        if (minute > 0) {
            return minute + "分钟";
        }
        if (second > 0) {
            return second + "秒";
        } else {
            return 0 + "秒";
        }
    }

}
