package com.hwenbin.server.service.impl;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.hwenbin.server.controller.workflow.flowmanage.req.PageQueryForWorkflowFormReq;
import com.hwenbin.server.core.mybatis.query.MyLambdaQueryWrapper;
import com.hwenbin.server.core.web.response.PageResult;
import com.hwenbin.server.core.web.response.ResultCode;
import com.hwenbin.server.entity.WorkflowFormEntity;
import com.hwenbin.server.mapper.WorkflowFormMapper;
import com.hwenbin.server.service.WorkflowFormService;
import com.hwenbin.server.util.AssertUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Collection;

/**
 * 流程表单Service业务层处理
 *
 * @author hwb
 * @date 2022/04/29 18:53
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class WorkflowFormServiceImpl implements WorkflowFormService {

    @Resource
    private WorkflowFormMapper workflowFormMapper;

    @Override
    public PageResult<WorkflowFormEntity> pageQuery(PageQueryForWorkflowFormReq req) {
        return workflowFormMapper.selectPage(
                req, new MyLambdaQueryWrapper<WorkflowFormEntity>()
                        .likeIfPresent(WorkflowFormEntity::getFormName, req.getFormName())
        );
    }

    @Override
    public WorkflowFormEntity getById(Long formId) {
        return checkWorkflowFormExists(formId);
    }

    @Override
    public void add(WorkflowFormEntity formEntity) {
        checkWorkflowFormUnique(formEntity.getFormName());
        workflowFormMapper.insert(formEntity);
    }

    @Override
    public void updateById(WorkflowFormEntity formEntity) {
        WorkflowFormEntity dbEntity = checkWorkflowFormExists(formEntity.getFormId());
        formEntity.setFormName(dbEntity.getFormName().equals(formEntity.getFormName()) ? null : formEntity.getFormName());
        checkWorkflowFormUnique(formEntity.getFormName());
        workflowFormMapper.updateById(formEntity);
    }

    @Override
    public void deleteByIds(Collection<Long> formIds) {
        workflowFormMapper.deleteBatchIds(formIds);
    }

    /**
     * 校验流程表单是否存在
     * @param formId 流程表单主键id
     * @return 流程表单实体对象
     */
    private WorkflowFormEntity checkWorkflowFormExists(Long formId) {
        WorkflowFormEntity formEntity = workflowFormMapper.selectById(formId);
        AssertUtils.asserts(ObjectUtil.isNotNull(formEntity), ResultCode.WORKFLOW_FORM_NOT_FOUND);
        return formEntity;
    }

    /**
     * 校验流程表单的唯一性（名称）
     * @param formName 表单名称
     */
    private void checkWorkflowFormUnique(String formName) {
        if (StrUtil.isNotBlank(formName)) {
            AssertUtils.asserts(
                    workflowFormMapper.selectCount(WorkflowFormEntity::getFormName, formName) == 0,
                    ResultCode.WORKFLOW_FORM_NAME_DUPLICATE
            );
        }
    }

}
