package com.hwenbin.server.entity;

import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * 流程任务业务对象
 *
 * @author hwb
 * @date 2022/05/01 16:03
 */
@Data
public class WorkflowTaskEntity {

    /**
     * 任务Id
     */
    private String taskId;

    /**
     * 用户Id
     */
    private String userId;

    /**
     * 任务意见
     */
    private String comment;

    /**
     * 流程实例Id
     */
    private String instanceId;

    /**
     * 节点
     */
    private String targetKey;

    /**
     * 流程变量信息
     */
    private Map<String, Object> values;

    /**
     * 审批人
     */
    private String assignee;

    /**
     * 候选人
     */
    private List<String> candidateUsers;

    /**
     * 审批组
     */
    private List<String> candidateGroups;

}
