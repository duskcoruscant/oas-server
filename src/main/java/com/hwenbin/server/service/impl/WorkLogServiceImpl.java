package com.hwenbin.server.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.ImmutableMap;
import com.hwenbin.server.controller.worklogcenter.req.AddWorkLogReq;
import com.hwenbin.server.controller.worklogcenter.req.PageQueryForReceiveLogReq;
import com.hwenbin.server.controller.worklogcenter.req.PageQueryForWorkLogReq;
import com.hwenbin.server.controller.worklogcenter.req.UpdateWorkLogReq;
import com.hwenbin.server.core.web.response.PageResult;
import com.hwenbin.server.core.web.response.ResultCode;
import com.hwenbin.server.core.web.response.SortingField;
import com.hwenbin.server.core.websocket.WebSocketServer;
import com.hwenbin.server.dto.WorkLogDTO;
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
import java.util.Date;
import java.util.Set;
import java.util.stream.Collectors;

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
    public PageResult<WorkLogDTO> pageQuery(PageQueryForWorkLogReq req) {
        IPage<WorkLogDTO> iPage = workLogMapper.pageQuery(
                MyBatisUtils.buildPage(req, ListUtil.of(new SortingField("create_time", SortingField.ORDER_DESC))),
                new QueryWrapper<WorkLogEntity>()
                        .eq("wl.create_emp_id", req.getEmpId())
                        .eq(ObjectUtil.isNotNull(req.getType()), "wl.type", req.getType())
                        .like(StrUtil.isNotBlank(req.getTitle()), "wl.title", req.getTitle())
                        .between(ObjectUtil.isNotNull(req.getBeginTime()), "wl.create_time",
                                req.getBeginTime(), req.getEndTime())
        );
        return new PageResult<>(iPage.getRecords(), iPage.getTotal());
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

            // 获取当前日志已有的通知人
            Set<Long> dbSendEmpIds =
                    workLogSendMapper.selectList("log_id", req.getId())
                            .stream().map(WorkLogSendEntity::getSendEmpId).collect(Collectors.toSet());
            // 需要新增的通知人
            Collection<Long> createSendEmpIds = CollUtil.subtract(req.getSendEmpIds(), dbSendEmpIds);
            // 需要删除的通知人
            Collection<Long> removeSendEmpIds = CollUtil.subtract(dbSendEmpIds, req.getSendEmpIds());
            // 对需要新增和移除的数据记录进行处理
            if (CollUtil.isNotEmpty(createSendEmpIds)) {
                workLogSendMapper.insertByLogIdAndSendEmpIds(req.getId(), createSendEmpIds);
            }
            if (CollUtil.isNotEmpty(removeSendEmpIds)) {
                workLogSendMapper.delete(
                        Wrappers.<WorkLogSendEntity>lambdaUpdate()
                                .eq(WorkLogSendEntity::getLogId, req.getId())
                                .in(WorkLogSendEntity::getSendEmpId, removeSendEmpIds)
                );
            }

            // 通知相关人员
            notifyRelateEmpHaveNewLog(dbWorkLog.getCreateEmpId(), req.getSendEmpIds(), "向您发送了一篇日志【"
                    + workLogEntity.getTitle() + "】：更新");
        } else {
            // 删除所有关联记录
            workLogSendMapper.deleteByMap(ImmutableMap.of("log_id", req.getId()));
        }

    }

    @Override
    public PageResult<WorkLogSendDTO> pageQueryForReceiveLog(PageQueryForReceiveLogReq req) {
        IPage<WorkLogSendDTO> iPage = workLogSendMapper.pageQuery(
                MyBatisUtils.buildPage(req, ListUtil.of(new SortingField("create_time", SortingField.ORDER_DESC))),
                Wrappers.<WorkLogSendDTO>query()
                        .eq("wls.send_emp_id", req.getEmpId())
                        .eq(ObjectUtil.isNotNull(req.getType()), "wl.type", req.getType())
                        .like(StrUtil.isNotBlank(req.getTitle()), "wl.title", req.getTitle())
                        .between(ObjectUtil.isNotNull(req.getBeginTime()), "wl.create_time",
                                req.getBeginTime(), req.getEndTime())
        );
        return new PageResult<>(iPage.getRecords(), iPage.getTotal());
    }

    @Override
    public void updateWorkLogSendById(WorkLogSendEntity workLogSendEntity) {
        // 校验操作的数据记录是否存在
        WorkLogSendEntity dbWorkLogSendEntity = workLogSendMapper.selectById(workLogSendEntity.getId());
        AssertUtils.asserts(dbWorkLogSendEntity != null, ResultCode.WORK_LOG_SEND_ENTITY_NOT_FOUND);
        boolean isComment = false;
        if (StrUtil.isNotBlank(workLogSendEntity.getComment())) {
            workLogSendEntity.setCommentTime(new Date());
            isComment = true;
        } else {
            workLogSendEntity.setComment(null);
        }
        workLogSendMapper.updateById(workLogSendEntity);
        // 发送消息通知，dbWorkLogSendEntity已校验不为null
        WorkLogEntity dbWorkLogEntity = workLogMapper.selectById(dbWorkLogSendEntity.getLogId());
        if (dbWorkLogEntity == null) {
            return;
        }
        String commenter = employeeService.getEmployeeById(dbWorkLogSendEntity.getSendEmpId()).getName();
        webSocketServer.sendMessageTo(dbWorkLogEntity.getCreateEmpId(),
                commenter + (isComment ? "评论" : "阅读") + "了你的日志（标题为"
                        + dbWorkLogEntity.getTitle() + "）");
    }

    @Override
    public WorkLogDTO getWorkLogWithCommentsById(Long id) {
        WorkLogEntity dbWorkLog = workLogMapper.selectById(id);
        AssertUtils.asserts(dbWorkLog != null, ResultCode.WORK_LOG_NOT_FOUND);
        return workLogMapper.getWorkLogWithCommentsById(id);
    }

    @Override
    public WorkLogDTO getWorkLogWithSendEmpIdsById(Long id) {
        WorkLogEntity dbWorkLog = workLogMapper.selectById(id);
        AssertUtils.asserts(dbWorkLog != null, ResultCode.WORK_LOG_NOT_FOUND);
        return workLogMapper.getWorkLogWithSendEmpIdsById(id);
    }

}
