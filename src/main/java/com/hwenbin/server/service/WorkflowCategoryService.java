package com.hwenbin.server.service;

import com.hwenbin.server.controller.workflow.flowmanage.req.PageQueryForWorkflowCategoryReq;
import com.hwenbin.server.core.web.response.PageResult;
import com.hwenbin.server.entity.WorkflowCategoryEntity;

import java.util.Collection;
import java.util.List;

/**
 * 流程分类Service接口
 *
 * @author hwb
 * @date 2022/04/29 13:51
 */
public interface WorkflowCategoryService {

    /**
     * 查询单个
     * @param categoryId 流程分类实体id
     * @return 流程分类实体
     */
    WorkflowCategoryEntity getById(Long categoryId);

    /**
     * 分页查询
     * @param req 查询参数 + 分页参数
     * @return 分页结果
     */
    PageResult<WorkflowCategoryEntity> pageQuery(PageQueryForWorkflowCategoryReq req);

    /**
     * 获取所有
     * @return 流程分类列表
     */
    List<WorkflowCategoryEntity> getAll();

    /**
     * 新增
     * @param categoryEntity 实体对象
     */
    void add(WorkflowCategoryEntity categoryEntity);

    /**
     * 修改
     * @param categoryEntity 实体对象
     */
    void updateById(WorkflowCategoryEntity categoryEntity);

    /**
     * 批量删除
     * @param categoryIds 流程分类主键id集合
     */
    void deleteByIds(Collection<Long> categoryIds);

}
