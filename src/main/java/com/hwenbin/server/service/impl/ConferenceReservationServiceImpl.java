package com.hwenbin.server.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.hwenbin.server.controller.conferencecenter.req.PageQueryForResListReq;
import com.hwenbin.server.controller.conferencecenter.req.PageQueryForReservationReq;
import com.hwenbin.server.core.constant.ProjectConstant;
import com.hwenbin.server.core.mybatis.query.MyLambdaQueryWrapper;
import com.hwenbin.server.core.web.response.PageResult;
import com.hwenbin.server.core.web.response.ResultCode;
import com.hwenbin.server.core.web.response.SortingField;
import com.hwenbin.server.dto.ConferenceReservationDTO;
import com.hwenbin.server.entity.ConferenceEquipmentEntity;
import com.hwenbin.server.entity.ConferenceReservationEntity;
import com.hwenbin.server.entity.ConferenceRoomEquipmentEntity;
import com.hwenbin.server.mapper.ConferenceEquipmentMapper;
import com.hwenbin.server.mapper.ConferenceReservationMapper;
import com.hwenbin.server.mapper.ConferenceRoomEquipmentMapper;
import com.hwenbin.server.service.ConferenceReservationService;
import com.hwenbin.server.util.AssertUtils;
import com.hwenbin.server.util.MyBatisUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Set;
import java.util.stream.Collectors;

import static com.hwenbin.server.enums.ConferenceProcessStatusEnum.*;

/**
 * @author hwb
 * @date 2022/04/20 22:45
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class ConferenceReservationServiceImpl implements ConferenceReservationService {

    @Resource
    private ConferenceReservationMapper conferenceReservationMapper;

    @Resource
    private ConferenceEquipmentMapper conferenceEquipmentMapper;

    @Resource
    private ConferenceRoomEquipmentMapper conferenceRoomEquipmentMapper;

    @Override
    public PageResult<ConferenceReservationDTO> pageQueryForReservation(PageQueryForReservationReq req) {
        Set<Long> selectRoomIds = null;
        // TODO : 改为联表查询
        if (CollUtil.isNotEmpty(req.getEquipmentNames())) {
            Set<Long> equipmentIds = conferenceEquipmentMapper.selectObjs(
                    new MyLambdaQueryWrapper<ConferenceEquipmentEntity>()
                            .select(ConferenceEquipmentEntity::getId)
                            .in(ConferenceEquipmentEntity::getType, req.getEquipmentNames())
            ).stream().map(o -> Long.valueOf(o.toString())).collect(Collectors.toSet());
            if (CollUtil.isNotEmpty(equipmentIds)) {
                selectRoomIds = conferenceRoomEquipmentMapper.selectObjs(
                        new MyLambdaQueryWrapper<ConferenceRoomEquipmentEntity>()
                                .select(ConferenceRoomEquipmentEntity::getConferenceRoomId)
                                .in(ConferenceRoomEquipmentEntity::getConferenceEquipmentId, equipmentIds)
                ).stream().map(o -> Long.valueOf(o.toString())).collect(Collectors.toSet());
            }
        }
        IPage<ConferenceReservationDTO> iPage =
                conferenceReservationMapper.pageQueryForReservation(
                        req.getResDate(), MyBatisUtils.buildPage(req,
                                ListUtil.of(new SortingField("croom.create_time", SortingField.ORDER_DESC))),
                        new QueryWrapper<ConferenceReservationDTO>()
                                .like(StrUtil.isNotBlank(req.getRoomCode()), "croom.code", req.getRoomCode())
                                .in(CollUtil.isNotEmpty(selectRoomIds), "croom.id", selectRoomIds)
                );
        return new PageResult<>(
                iPage.getRecords(), iPage.getTotal()
        );
    }

    @Override
    public void add(ConferenceReservationEntity entity) {
        conferenceReservationMapper.insert(entity);
    }

    @Override
    public PageResult<ConferenceReservationEntity> pageQueryForResHistory(PageQueryForResListReq req) {
        IPage<ConferenceReservationEntity> iPage =
                conferenceReservationMapper.pageQueryForResHistory(
                        req.getProcessStatus(), MyBatisUtils.buildPage(req,
                                ListUtil.of(new SortingField("cr.create_time", SortingField.ORDER_DESC))),
                        new MyLambdaQueryWrapper<ConferenceReservationEntity>()
                                .eqIfPresent(ConferenceReservationEntity::getResEmpId, req.getResEmpId())
                                .likeIfPresent(ConferenceReservationEntity::getRoomCode, req.getRoomCode())
                                .likeIfPresent(ConferenceReservationEntity::getSubject, req.getSubject())
                                .betweenIfPresent(ConferenceReservationEntity::getDate,
                                        req.getBeginTime(), req.getEndTime())
                );
        LocalDateTime now = LocalDateTime.now();
        // 写 H:mm 而不是 HH:mm，以避免（0~9 : 00）解析异常
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("H:mm");
        iPage.getRecords().forEach(entity -> {
            LocalDateTime startDateTime =
                    entity.getDate().atTime(LocalTime.parse(entity.getStartTime(), formatter));
            LocalDateTime endDateTime;
            if (ProjectConstant.TWENTY_FOUR_CLOCK.equals(entity.getEndTime())) {
                // 处理结束时间为24:00的情况，否则LocalTime.parse会变为00:00
                endDateTime = LocalDateTime.of(entity.getDate(), LocalTime.MAX);
            } else {
                endDateTime = entity.getDate().atTime(LocalTime.parse(entity.getEndTime(), formatter));
            }
            // 会议进行状态
            entity.setProcessStatus(
                    now.isBefore(startDateTime) ? NONE_START.getStatus()
                            : now.isAfter(endDateTime) ? COMPLETED.getStatus()
                            : PROCESSING.getStatus()
            );
        });
        return new PageResult<>(iPage.getRecords(), iPage.getTotal());
    }

    @Override
    public void delete(Long id) {
        checkConferenceResPermitDelete(id);
        conferenceReservationMapper.deleteById(id);
    }

    /**
     * 校验会议是否存在
     * @param id 会议id
     * @return 会议实体数据
     */
    private ConferenceReservationEntity checkConferenceResExists(Long id) {
        ConferenceReservationEntity entity = conferenceReservationMapper.selectById(id);
        AssertUtils.asserts(entity != null, ResultCode.CONFERENCE_RESERVATION_NOT_FOUND);
        return entity;
    }

    /**
     * 检查会议是否允许删除（只允许删除未开始的会议）
     * @param id 会议id
     */
    private void checkConferenceResPermitDelete(Long id) {
        ConferenceReservationEntity entity = checkConferenceResExists(id);
        LocalDateTime startDateTime =
                entity.getDate().atTime(LocalTime.parse(entity.getStartTime(), DateTimeFormatter.ofPattern("H:mm")));
        // 会议当前状态必须为未开始，否则抛出异常
        AssertUtils.asserts(LocalDateTime.now().isBefore(startDateTime),
                ResultCode.CONFERENCE_RESERVATION_NOT_PERMIT_DELETE);
    }

}
