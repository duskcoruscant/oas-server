package com.hwenbin.server.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.hwenbin.server.controller.missivecenter.req.PageQueryForMissiveReq;
import com.hwenbin.server.core.web.response.PageResult;
import com.hwenbin.server.core.web.response.ResultCode;
import com.hwenbin.server.core.web.response.SortingField;
import com.hwenbin.server.core.websocket.WebSocketServer;
import com.hwenbin.server.dto.MissiveDTO;
import com.hwenbin.server.entity.Employee;
import com.hwenbin.server.entity.MissiveEntity;
import com.hwenbin.server.enums.MissiveSecretLevelEnum;
import com.hwenbin.server.mapper.MissiveMapper;
import com.hwenbin.server.service.EmployeeService;
import com.hwenbin.server.service.MissiveService;
import com.hwenbin.server.util.AssertUtils;
import com.hwenbin.server.util.MyBatisUtils;
import com.hwenbin.server.util.ThreadPoolUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author hwb
 * @date 2022/04/17 23:45
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class MissiveServiceImpl implements MissiveService {

    @Resource
    private MissiveMapper missiveMapper;

    @Resource
    private EmployeeService employeeService;

    @Resource
    private WebSocketServer webSocketServer;

    @Override
    public PageResult<MissiveDTO> pageQuery(PageQueryForMissiveReq req) {
        Long checkDeptId = employeeService.getEmployeeById(req.getEmpId()).getDeptId();
        IPage<MissiveDTO> iPage = missiveMapper.pageQuery(
                MyBatisUtils.buildPage(req, ListUtil.of(new SortingField("create_time", SortingField.ORDER_DESC))),
                new QueryWrapper<MissiveDTO>()
                        .like(ObjectUtil.isNotNull(req.getId()), "m.id", req.getId())
                        .like(StrUtil.isNotEmpty(req.getName()), "m.name", req.getName())
                        .eq(ObjectUtil.isNotNull(req.getType()), "m.type", req.getType())
                        .eq(StrUtil.isNotEmpty(req.getAuthorId()), "m.author_id", req.getAuthorId())
                        .between(ObjectUtil.isNotNull(req.getBeginTime()), "m.create_time",
                                req.getBeginTime(), req.getEndTime())
                        .and(
                                wrapper -> wrapper.eq("m.secret_level", MissiveSecretLevelEnum.PUBLIC.getLevel())
                                        .or().eq("m.primary_send_dept_id", checkDeptId)
                                        .or().eq("m.copy_send_dept_id", checkDeptId)
                        )
        );
        return new PageResult<>(iPage.getRecords(), iPage.getTotal());
    }

    @Override
    public MissiveEntity getById(Long id) {
        return checkMissiveExists(id);
    }

    @Override
    public void add(MissiveEntity entity) {
        missiveMapper.insert(entity);
        // TODO : 附件
        // 通知相关人员
        noticeMissiveRelatedEmp(entity);
    }

    @Override
    public void update(MissiveEntity entity) {
        checkMissiveExists(entity.getId());
        missiveMapper.updateById(entity);
        // TODO : 附件
        noticeMissiveRelatedEmp(entity);
    }

    @Override
    public void deleteById(Long id) {
        checkMissiveExists(id);
        missiveMapper.deleteById(id);
        // TODO : 附件
    }

    /**
     * 开启后台线程，通知公文相关人员
     * @param entity 公文实体
     */
    private void noticeMissiveRelatedEmp(MissiveEntity entity) {
        ThreadPoolUtil.submit(() -> {
            Set<Long> noticeEmpIds = null;
            if (MissiveSecretLevelEnum.PUBLIC.getLevel().equals(entity.getSecretLevel())) {
                // 通知所有人
                noticeEmpIds =
                        employeeService.list().stream().map(Employee::getId).collect(Collectors.toSet());
            } else if (MissiveSecretLevelEnum.DEPARTMENT.getLevel().equals(entity.getSecretLevel())) {
                // 通知主送部门与抄送部门人员
                noticeEmpIds =
                        employeeService.getAllEmpIdByDeptId(
                                ListUtil.of(entity.getPrimarySendDeptId(), entity.getCopySendDeptId())
                        );
            }
            if (CollUtil.isEmpty(noticeEmpIds)) {
                return;
            }
            String authorName = employeeService.getEmployeeById(entity.getAuthorId()).getName();
            String message = "您收到了一份" + authorName + "拟稿的公文：" + entity.getName();
            noticeEmpIds.forEach(empId -> webSocketServer.sendMessageTo(empId, message));
        });
    }

    /**
     * 校验公文是否存在
     * @param id 公文id
     * @return 公文实体
     */
    private MissiveEntity checkMissiveExists(Long id) {
        MissiveEntity entity = missiveMapper.selectById(id);
        AssertUtils.asserts(ObjectUtil.isNotNull(entity), ResultCode.MISSIVE_NOT_FOUND);
        return entity;
    }

}
