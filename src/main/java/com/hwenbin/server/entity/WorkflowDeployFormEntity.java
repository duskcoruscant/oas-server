package com.hwenbin.server.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * 流程实例关联表单 实体
 *
 * @author hwb
 * @date 2022/04/30 20:01
 */
@Data
@TableName("workflow_deploy_form")
public class WorkflowDeployFormEntity {

    /**
     * 流程定义主键
     */
    @TableId(type = IdType.INPUT)
    private String deployId;

    /**
     * 表单主键
     */
    private Long formId;

}
