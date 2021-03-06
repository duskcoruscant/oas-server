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
        // TODO : ??????????????????
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
        // ??? H:mm ????????? HH:mm???????????????0~9 : 00???????????????
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("H:mm");
        iPage.getRecords().forEach(entity -> {
            LocalDateTime startDateTime =
                    entity.getDate().atTime(LocalTime.parse(entity.getStartTime(), formatter));
            LocalDateTime endDateTime;
            if (ProjectConstant.TWENTY_FOUR_CLOCK.equals(entity.getEndTime())) {
                // ?????????????????????24:00??????????????????LocalTime.parse?????????00:00
                endDateTime = LocalDateTime.of(entity.getDate(), LocalTime.MAX);
            } else {
                endDateTime = entity.getDate().atTime(LocalTime.parse(entity.getEndTime(), formatter));
            }
            // ??????????????????
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
     * ????????????????????????
     * @param id ??????id
     * @return ??????????????????
     */
    private ConferenceReservationEntity checkConferenceResExists(Long id) {
        ConferenceReservationEntity entity = conferenceReservationMapper.selectById(id);
        AssertUtils.asserts(entity != null, ResultCode.CONFERENCE_RESERVATION_NOT_FOUND);
        return entity;
    }

    /**
     * ?????????????????????????????????????????????????????????????????????
     * @param id ??????id
     */
    private void checkConferenceResPermitDelete(Long id) {
        ConferenceReservationEntity entity = checkConferenceResExists(id);
        LocalDateTime startDateTime =
                entity.getDate().atTime(LocalTime.parse(entity.getStartTime(), DateTimeFormatter.ofPattern("H:mm")));
        // ?????????????????????????????????????????????????????????
        AssertUtils.asserts(LocalDateTime.now().isBefore(startDateTime),
                ResultCode.CONFERENCE_RESERVATION_NOT_PERMIT_DELETE);
    }

}
