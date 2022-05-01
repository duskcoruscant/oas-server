package com.hwenbin.server.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.hwenbin.server.core.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 流程分类实体
 *
 * @author hwb
 * @date 2022/04/29 12:35
 */
@EqualsAndHashCode(callSuper = true)
@Data
@TableName("workflow_category")
public class WorkflowCategoryEntity extends BaseEntity {

    /**
     * 流程分类id
     */
    @TableId
    private Long categoryId;

    /**
     * 流程分类名称
     */
    private String categoryName;

    /**
     * 分类编码
     */
    private String code;

    /**
     * 备注
     */
    private String remark;

}
