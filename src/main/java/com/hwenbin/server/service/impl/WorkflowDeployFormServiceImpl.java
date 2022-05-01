package com.hwenbin.server.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.hwenbin.server.core.exception.ServiceException;
import com.hwenbin.server.entity.WorkflowDeployFormEntity;
import com.hwenbin.server.entity.WorkflowFormEntity;
import com.hwenbin.server.mapper.WorkflowDeployFormMapper;
import com.hwenbin.server.mapper.WorkflowFormMapper;
import com.hwenbin.server.service.WorkflowDeployFormService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
 * 流程实例关联表单Service业务层处理
 *
 * @author hwb
 * @date 2022/04/30 20:11
 */
@Service
public class WorkflowDeployFormServiceImpl implements WorkflowDeployFormService {

    @Resource
    private WorkflowDeployFormMapper workflowDeployFormMapper;

    @Resource
    private WorkflowFormMapper workflowFormMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Integer insertWorkflowDeployForm(WorkflowDeployFormEntity workflowDeployFormEntity) {
        // 删除部署流程和表单的关联关系
        workflowDeployFormMapper.delete(
                new LambdaQueryWrapper<WorkflowDeployFormEntity>()
                        .eq(WorkflowDeployFormEntity::getDeployId, workflowDeployFormEntity.getDeployId())
        );
        // 新增部署流程和表单关系
        return workflowDeployFormMapper.insert(workflowDeployFormEntity);
    }

    @Override
    public WorkflowFormEntity selectDeployFormByDeployId(String deployId) {
        QueryWrapper<WorkflowFormEntity> wrapper = Wrappers.query();
        wrapper.eq("wdf.deploy_id", deployId);
        List<WorkflowFormEntity> list = workflowFormMapper.selectFormList(wrapper);
        if (ObjectUtil.isNotEmpty(list)) {
            if (list.size() != 1) {
                throw new ServiceException("表单信息查询错误");
            } else {
                return list.get(0);
            }
        } else {
            return null;
        }
    }

}
