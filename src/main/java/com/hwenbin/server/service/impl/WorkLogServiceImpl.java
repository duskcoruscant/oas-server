package com.hwenbin.server.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.ImmutableMap;
import com.hwenbin.server.controller.worklogcenter.req.AddWorkLogReq;
import com.hwenbin.server.controller.worklogcenter.req.PageQueryForReceiveLogReq;
import com.hwenbin.server.controller.worklogcenter.req.PageQueryForWorkLogReq;
import com.hwenbin.server.controller.worklogcenter.req.UpdateWorkLogReq;
import com.hwenbin.server.core.mybatis.query.MyLambdaQueryWrapper;
import com.hwenbin.server.core.web.response.PageResult;
import com.hwenbin.server.core.web.response.ResultCode;
import com.hwenbin.server.dto.WorkLogSendDTO;
import com.hwenbin.server.entity.WorkLogEntity;
import com.hwenbin.server.entity.WorkLogSendEntity;
import com.hwenbin.server.mapper.WorkLogMapper;
import com.hwenbin.server.mapper.WorkLogSendMapper;
import com.hwenbin.server.service.WorkLogService;
import com.hwenbin.server.util.AssertUtils;
import com.hwenbin.server.util.MyBatisUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
 * @author hwb
 * @date 2022/04/10 21:17
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class WorkLogServiceImpl extends ServiceImpl<WorkLogMapper, WorkLogEntity> implements WorkLogService {

    @Resource
    private WorkLogMapper workLogMapper;

    @Resource
    private WorkLogSendMapper workLogSendMapper;

    @Override
    public PageResult<WorkLogEntity> pageQuery(PageQueryForWorkLogReq req) {
        return workLogMapper.selectPage(
                req, new MyLambdaQueryWrapper<WorkLogEntity>()
                        .eq(WorkLogEntity::getCreateEmpId, req.getEmpId())
                        .eqIfPresent(WorkLogEntity::getType, req.getType())
                        .betweenIfPresent(WorkLogEntity::getCreateTime, req.getBeginTime(), req.getEndTime())
        );
    }

    @Override
    public void add(AddWorkLogReq req) {
        WorkLogEntity workLogEntity = new WorkLogEntity();
        BeanUtil.copyProperties(req, workLogEntity);
        workLogMapper.insert(workLogEntity);
        if (CollUtil.isNotEmpty(req.getSendEmpIds())) {
            // 新增关联记录
            workLogSendMapper.insertByLogIdAndSendEmpIds(workLogEntity.getId(), req.getSendEmpIds());
        }
    }

    @Override
    public void updateById(UpdateWorkLogReq req) {
        AssertUtils.asserts(workLogMapper.selectById(req.getId()) != null, ResultCode.WORK_LOG_NOT_FOUND);
        WorkLogEntity workLogEntity = new WorkLogEntity();
        BeanUtil.copyProperties(req, workLogEntity);
        workLogMapper.updateById(workLogEntity);
        if (CollUtil.isNotEmpty(req.getSendEmpIds())) {
            // 先删除所有关联记录，再添加
            workLogSendMapper.deleteByMap(ImmutableMap.of("log_id", req.getId()));
            workLogSendMapper.insertByLogIdAndSendEmpIds(req.getId(), req.getSendEmpIds());
        }
    }

    @Override
    public PageResult<WorkLogSendDTO> pageQueryForReceiveLog(PageQueryForReceiveLogReq req) {
        IPage<WorkLogSendDTO> iPage = workLogSendMapper.pageQuery(
                MyBatisUtils.buildPage(req),
                new MyLambdaQueryWrapper<WorkLogSendEntity>().eq(WorkLogSendEntity::getSendEmpId, req.getEmpId())
        );
        return new PageResult<>(iPage.getRecords(), iPage.getTotal());
    }

}
