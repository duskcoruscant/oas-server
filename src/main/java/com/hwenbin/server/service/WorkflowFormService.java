package com.hwenbin.server.service;

import com.hwenbin.server.controller.workflow.flowmanage.req.PageQueryForWorkflowFormReq;
import com.hwenbin.server.core.web.response.PageResult;
import com.hwenbin.server.entity.WorkflowFormEntity;

import java.util.Collection;

/**
 * 流程表单Service接口
 *
 * @author hwb
 * @date 2022/04/29 18:52
 */
public interface WorkflowFormService {

    /**
     * 分页查询
     * @param req 查询参数 + 分页参数
     * @return 分页结果
     */
    PageResult<WorkflowFormEntity> pageQuery(PageQueryForWorkflowFormReq req);

    /**
     * 查询单个
     * @param formId 流程表单实体id
     * @return 流程表单实体
     */
    WorkflowFormEntity getById(Long formId);

    /**
     * 新增
     * @param formEntity 实体对象
     */
    void add(WorkflowFormEntity formEntity);

    /**
     * 修改
     * @param formEntity 实体对象
     */
    void updateById(WorkflowFormEntity formEntity);

    /**
     * 批量删除
     * @param formIds 流程表单主键id集合
     */
    void deleteByIds(Collection<Long> formIds);

}
