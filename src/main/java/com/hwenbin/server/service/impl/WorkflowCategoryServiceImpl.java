package com.hwenbin.server.service.impl;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.hwenbin.server.controller.workflow.flowmanage.req.PageQueryForWorkflowCategoryReq;
import com.hwenbin.server.core.mybatis.query.MyLambdaQueryWrapper;
import com.hwenbin.server.core.web.response.PageResult;
import com.hwenbin.server.core.web.response.ResultCode;
import com.hwenbin.server.entity.WorkflowCategoryEntity;
import com.hwenbin.server.mapper.WorkflowCategoryMapper;
import com.hwenbin.server.service.WorkflowCategoryService;
import com.hwenbin.server.util.AssertUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Collection;
import java.util.List;

/**
 * 流程分类Service业务层处理
 *
 * @author hwb
 * @date 2022/04/29 13:57
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class WorkflowCategoryServiceImpl implements WorkflowCategoryService {

    @Resource
    private WorkflowCategoryMapper workflowCategoryMapper;

    @Override
    public WorkflowCategoryEntity getById(Long categoryId) {
        return checkWorkflowCategoryExists(categoryId);
    }

    @Override
    public PageResult<WorkflowCategoryEntity> pageQuery(PageQueryForWorkflowCategoryReq req) {
        return workflowCategoryMapper.selectPage(
                req, new MyLambdaQueryWrapper<WorkflowCategoryEntity>()
                        .likeIfPresent(WorkflowCategoryEntity::getCategoryName, req.getCategoryName())
                        .likeIfPresent(WorkflowCategoryEntity::getCode, req.getCode())
        );
    }

    @Override
    public List<WorkflowCategoryEntity> getAll() {
        return workflowCategoryMapper.selectList();
    }

    @Override
    public void add(WorkflowCategoryEntity categoryEntity) {
        checkWorkflowCategoryUnique(categoryEntity.getCategoryName(), categoryEntity.getCode());
        workflowCategoryMapper.insert(categoryEntity);
    }

    @Override
    public void updateById(WorkflowCategoryEntity categoryEntity) {
        WorkflowCategoryEntity dbEntity = checkWorkflowCategoryExists(categoryEntity.getCategoryId());
        categoryEntity.setCategoryName(
                dbEntity.getCategoryName().equals(categoryEntity.getCategoryName())
                        ? null : categoryEntity.getCategoryName());
        categoryEntity.setCode(dbEntity.getCode().equals(categoryEntity.getCode()) ? null : categoryEntity.getCode());
        checkWorkflowCategoryUnique(categoryEntity.getCategoryName(), categoryEntity.getCode());
        workflowCategoryMapper.updateById(categoryEntity);
    }

    @Override
    public void deleteByIds(Collection<Long> categoryIds) {
        workflowCategoryMapper.deleteBatchIds(categoryIds);
    }

    /**
     * 校验流程分类是否存在
     * @param categoryId 流程分类主键id
     * @return 流程分类实体
     */
    private WorkflowCategoryEntity checkWorkflowCategoryExists(Long categoryId) {
        WorkflowCategoryEntity categoryEntity = workflowCategoryMapper.selectById(categoryId);
        AssertUtils.asserts(ObjectUtil.isNotNull(categoryEntity), ResultCode.WORKFLOW_CATEGORY_NOT_FOUND);
        return categoryEntity;
    }

    /**
     * 校验流程分类的唯一性（名称 + 编码）
     * @param name 名称
     * @param code 编码
     */
    private void checkWorkflowCategoryUnique(String name, String code) {
        if (StrUtil.isNotBlank(name)) {
            AssertUtils.asserts(
                    workflowCategoryMapper.selectCount(WorkflowCategoryEntity::getCategoryName, name) == 0,
                    ResultCode.WORKFLOW_CATEGORY_NAME_DUPLICATE
            );
        }
        if (StrUtil.isNotBlank(code)) {
            AssertUtils.asserts(
                    workflowCategoryMapper.selectCount(WorkflowCategoryEntity::getCode, code) == 0,
                    ResultCode.WORKFLOW_CATEGORY_CODE_DUPLICATE
            );
        }
    }

}
