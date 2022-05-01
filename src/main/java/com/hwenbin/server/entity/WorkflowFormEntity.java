package com.hwenbin.server.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.hwenbin.server.core.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 流程表单实体
 *
 * @author hwb
 * @date 2022/04/29 18:50
 */
@EqualsAndHashCode(callSuper = true)
@Data
@TableName("workflow_form")
public class WorkflowFormEntity extends BaseEntity {

    /**
     * 表单主键id
     */
    @TableId
    private Long formId;

    /**
     * 表单名称
     */
    private String formName;

    /**
     * 表单内容
     */
    private String content;

    /**
     * 备注
     */
    private String remark;

}
