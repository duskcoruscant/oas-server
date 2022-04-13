package com.hwenbin.server.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ListUtil;
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
import com.hwenbin.server.core.web.response.SortingField;
import com.hwenbin.server.core.websocket.WebSocketServer;
import com.hwenbin.server.dto.WorkLogSendDTO;
import com.hwenbin.server.entity.WorkLogEntity;
import com.hwenbin.server.entity.WorkLogSendEntity;
import com.hwenbin.server.mapper.WorkLogMapper;
import com.hwenbin.server.mapper.WorkLogSendMapper;
import com.hwenbin.server.service.EmployeeService;
import com.hwenbin.server.service.WorkLogService;
import com.hwenbin.server.util.AssertUtils;
import com.hwenbin.server.util.MyBatisUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Collection;

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

    @Resource
    private EmployeeService employeeService;

    @Resource
    private WebSocketServer webSocketServer;

    @Override
    public PageResult<WorkLogEntity> pageQuery(PageQueryForWorkLogReq req) {
        return workLogMapper.selectPage(
                req, ListUtil.of(new SortingField("create_time", SortingField.ORDER_DESC)),
                new MyLambdaQueryWrapper<WorkLogEntity>()
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
            // 通知相关人员
            notifyRelateEmpHaveNewLog(req.getCreateEmpId(), req.getSendEmpIds(), "向您发送了一篇日志【"
                    + workLogEntity.getTitle() + "】：新建");
        }
    }

    /**
     * 给相关人员 所在客户端 发送消息
     *
     * @param from    发送人
     * @param tos     接收人
     * @param message 消息
     */
    private void notifyRelateEmpHaveNewLog(Long from, Collection<Long> tos, String message) {
        String newMsg = "您的同事【" + employeeService.getEmployeeById(from).getName() +
                "】" + message;
        tos.forEach(id -> webSocketServer.sendMessageTo(id, newMsg));
    }

    @Override
    public void updateById(UpdateWorkLogReq req) {
        WorkLogEntity dbWorkLog = workLogMapper.selectById(req.getId());
        AssertUtils.asserts(dbWorkLog != null, ResultCode.WORK_LOG_NOT_FOUND);
        WorkLogEntity workLogEntity = new WorkLogEntity();
        BeanUtil.copyProperties(req, workLogEntity);
        workLogMapper.updateById(workLogEntity);
        if (CollUtil.isNotEmpty(req.getSendEmpIds())) {
            // 先删除所有关联记录，再添加
            workLogSendMapper.deleteByMap(ImmutableMap.of("log_id", req.getId()));
            workLogSendMapper.insertByLogIdAndSendEmpIds(req.getId(), req.getSendEmpIds());
            // 通知相关人员
            notifyRelateEmpHaveNewLog(dbWorkLog.getCreateEmpId(), req.getSendEmpIds(), "向您发送了一篇日志【"
                    + workLogEntity.getTitle() + "】：更新");
        }
    }

    @Override
    public PageResult<WorkLogSendDTO> pageQueryForReceiveLog(PageQueryForReceiveLogReq req) {
        IPage<WorkLogSendDTO> iPage = workLogSendMapper.pageQuery(
                MyBatisUtils.buildPage(req, ListUtil.of(new SortingField("create_time", SortingField.ORDER_DESC))),
                new MyLambdaQueryWrapper<WorkLogSendEntity>().eq(WorkLogSendEntity::getSendEmpId, req.getEmpId())
        );
        return new PageResult<>(iPage.getRecords(), iPage.getTotal());
    }

}
