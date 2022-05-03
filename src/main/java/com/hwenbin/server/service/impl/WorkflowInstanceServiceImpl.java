package com.hwenbin.server.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.BetweenFormatter;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import com.hwenbin.server.core.constant.TaskConstant;
import com.hwenbin.server.core.exception.ServiceException;
import com.hwenbin.server.core.flowable.factory.FlowServiceFactory;
import com.hwenbin.server.dto.flow.WorkflowTaskDTO;
import com.hwenbin.server.entity.*;
import com.hwenbin.server.service.*;
import com.hwenbin.server.util.JsonUtils;
import com.hwenbin.server.util.StringUtils;
import org.flowable.engine.history.HistoricProcessInstance;
import org.flowable.engine.task.Comment;
import org.flowable.identitylink.api.history.HistoricIdentityLink;
import org.flowable.task.api.history.HistoricTaskInstance;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;

/**
 * 工作流流程实例管理
 *
 * @author hwb
 * @date 2022/05/02 21:11
 */
@Service
public class WorkflowInstanceServiceImpl extends FlowServiceFactory implements WorkflowInstanceService {

    @Resource
    private WorkflowDeployFormService deployFormService;

    @Resource
    private EmployeeService employeeService;

    @Resource
    private RoleService roleService;

    @Resource
    private DepartmentService departmentService;

    @Override
    public void stopProcessInstance(WorkflowTaskEntity vo) {
        String taskId = vo.getTaskId();

    }

    @Override
    public void updateState(Integer state, String instanceId) {

        // 激活
        if (state == 1) {
            runtimeService.activateProcessInstanceById(instanceId);
        }
        // 挂起
        if (state == 2) {
            runtimeService.suspendProcessInstanceById(instanceId);
        }

    }

    @Override
    public void delete(String instanceId, String deleteReason) {

        // 查询历史数据
        HistoricProcessInstance historicProcessInstance = getHistoricProcessInstanceById(instanceId);
        if (historicProcessInstance.getEndTime() != null) {
            historyService.deleteHistoricProcessInstance(historicProcessInstance.getId());
            return;
        }
        // 删除流程实例
        runtimeService.deleteProcessInstance(instanceId, deleteReason);
        // 删除历史流程实例
        historyService.deleteHistoricProcessInstance(instanceId);

    }

    @Override
    public HistoricProcessInstance getHistoricProcessInstanceById(String processInstanceId) {
        HistoricProcessInstance historicProcessInstance =
                historyService.createHistoricProcessInstanceQuery().processInstanceId(processInstanceId).singleResult();
        if (Objects.isNull(historicProcessInstance)) {
            // throw new FlowableObjectNotFoundException("流程实例不存在: " + processInstanceId);
            throw new ServiceException("流程实例不存在: " + processInstanceId);
        }
        return historicProcessInstance;
    }

    @Override
    public Map<String, Object> queryDetailProcess(String procInsId, String deployId) {
        Map<String, Object> map = new HashMap<>();
        if (StringUtils.isNotBlank(procInsId)) {
            List<HistoricTaskInstance> taskInstanceList = historyService.createHistoricTaskInstanceQuery()
                    .processInstanceId(procInsId)
                    .orderByHistoricTaskInstanceStartTime().desc()
                    .list();
            List<Comment> commentList = taskService.getProcessInstanceComments(procInsId);
            List<WorkflowTaskDTO> taskVoList = new ArrayList<>(taskInstanceList.size());
            taskInstanceList.forEach(taskInstance -> {
                WorkflowTaskDTO taskVo = new WorkflowTaskDTO();
                taskVo.setProcDefId(taskInstance.getProcessDefinitionId());
                taskVo.setTaskId(taskInstance.getId());
                taskVo.setTaskName(taskInstance.getName());
                taskVo.setCreateTime(taskInstance.getStartTime());
                taskVo.setFinishTime(taskInstance.getEndTime());
                if (StringUtils.isNotBlank(taskInstance.getAssignee())) {
                    Employee user = employeeService.getEmployeeById(Long.parseLong(taskInstance.getAssignee()));
                    taskVo.setAssigneeId(user.getId());
                    taskVo.setAssigneeName(user.getName());
                    taskVo.setDeptName(departmentService.getById(user.getDeptId()).getName());
                }
                // 展示审批人员
                List<HistoricIdentityLink> linksForTask = historyService.getHistoricIdentityLinksForTask(taskInstance.getId());
                StringBuilder stringBuilder = new StringBuilder();
                for (HistoricIdentityLink identityLink : linksForTask) {
                    if ("candidate".equals(identityLink.getType())) {
                        if (StringUtils.isNotBlank(identityLink.getUserId())) {
                            Employee user = employeeService.getEmployeeById(Long.parseLong(identityLink.getUserId()));
                            stringBuilder.append(user.getName()).append(",");
                        }
                        if (StringUtils.isNotBlank(identityLink.getGroupId())) {
                            if (identityLink.getGroupId().startsWith(TaskConstant.ROLE_GROUP_PREFIX)) {
                                Long roleId = Long.parseLong(StringUtils.stripStart(identityLink.getGroupId(), TaskConstant.ROLE_GROUP_PREFIX));
                                Role role = roleService.getRoleById(roleId);
                                stringBuilder.append(role.getName()).append(",");
                            } else if (identityLink.getGroupId().startsWith(TaskConstant.DEPT_GROUP_PREFIX)) {
                                Long deptId = Long.parseLong(StringUtils.stripStart(identityLink.getGroupId(), TaskConstant.DEPT_GROUP_PREFIX));
                                Department dept = departmentService.getById(deptId);
                                stringBuilder.append(dept.getName()).append(",");
                            }
                        }
                    }
                }
                if (StringUtils.isNotBlank(stringBuilder)) {
                    taskVo.setCandidate(stringBuilder.substring(0, stringBuilder.length() - 1));
                }
                if (ObjectUtil.isNotNull(taskInstance.getDurationInMillis())) {
                    taskVo.setDuration(DateUtil.formatBetween(taskInstance.getDurationInMillis(), BetweenFormatter.Level.SECOND));
                }
                // 获取意见评论内容
                if (CollUtil.isNotEmpty(commentList)) {
                    List<Comment> comments = new ArrayList<>();
                    // commentList.stream().filter(comment -> taskInstance.getId().equals(comment.getTaskId())).collect(Collectors.toList());
                    for (Comment comment : commentList) {
                        if (comment.getTaskId().equals(taskInstance.getId())) {
                            comments.add(comment);
                            // taskVo.setComment(WfCommentDto.builder().type(comment.getType()).comment(comment.getFullMessage()).build());
                        }
                    }
                    taskVo.setCommentList(comments);
                }
                taskVoList.add(taskVo);
            });
            map.put("flowList", taskVoList);
//            // 查询当前任务是否完成
//            List<Task> taskList = taskService.createTaskQuery().processInstanceId(procInsId).list();
//            if (CollectionUtils.isNotEmpty(taskList)) {
//                map.put("finished", true);
//            } else {
//                map.put("finished", false);
//            }
        }
        // 第一次申请获取初始化表单
        if (StringUtils.isNotBlank(deployId)) {
            WorkflowFormEntity formEntity = deployFormService.selectDeployFormByDeployId(deployId);
            if (Objects.isNull(formEntity)) {
                throw new ServiceException("请先配置流程表单");
            }
            map.put("formData", JsonUtils.parseObject(formEntity.getContent(), Map.class));
        }
        return map;
    }

}
