package com.hwenbin.server.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ListUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.hwenbin.server.controller.conferencecenter.req.PageQueryForConferenceRoomReq;
import com.hwenbin.server.core.mybatis.query.MyLambdaQueryWrapper;
import com.hwenbin.server.core.web.response.PageResult;
import com.hwenbin.server.core.web.response.ResultCode;
import com.hwenbin.server.core.web.response.SortingField;
import com.hwenbin.server.dto.ConferenceRoomDTO;
import com.hwenbin.server.entity.ConferenceEquipmentEntity;
import com.hwenbin.server.entity.ConferenceRoomEntity;
import com.hwenbin.server.entity.ConferenceRoomEquipmentEntity;
import com.hwenbin.server.enums.CommonStatusEnum;
import com.hwenbin.server.enums.ConferenceEquipmentStatusEnum;
import com.hwenbin.server.mapper.ConferenceEquipmentMapper;
import com.hwenbin.server.mapper.ConferenceReservationMapper;
import com.hwenbin.server.mapper.ConferenceRoomEquipmentMapper;
import com.hwenbin.server.mapper.ConferenceRoomMapper;
import com.hwenbin.server.service.ConferenceRoomService;
import com.hwenbin.server.util.AssertUtils;
import com.hwenbin.server.util.MyBatisUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author hwb
 * @date 2022/04/14 13:39
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class ConferenceRoomServiceImpl implements ConferenceRoomService {

    @Resource
    private ConferenceRoomMapper conferenceRoomMapper;

    @Resource
    private ConferenceEquipmentMapper conferenceEquipmentMapper;

    @Resource
    private ConferenceRoomEquipmentMapper conferenceRoomEquipmentMapper;

    @Resource
    private ConferenceReservationMapper conferenceReservationMapper;

    @Override
    public PageResult<ConferenceRoomDTO> pageQuery(PageQueryForConferenceRoomReq req) {
        Set<Long> selectRoomIds = null;
        if (CollUtil.isNotEmpty(req.getConferenceEquipmentTypes())) {
            // ?????????????????????????????????????????????????????????????????????id??????????????????????????????????????????????????????id????????????id???
            // ????????????in????????????????????? TODO : ??????????????????
            Set<Long> equipmentIds =
                    conferenceEquipmentMapper.selectList("type", req.getConferenceEquipmentTypes())
                            .stream().map(ConferenceEquipmentEntity::getId).collect(Collectors.toSet());
            selectRoomIds =
                    conferenceRoomEquipmentMapper.selectList("conference_equipment_id", equipmentIds)
                            .stream().map(ConferenceRoomEquipmentEntity::getConferenceRoomId).collect(Collectors.toSet());
        }
        IPage<ConferenceRoomDTO> iPage = conferenceRoomMapper.pageQuery(
                MyBatisUtils.buildPage(req, ListUtil.of(new SortingField("create_time", SortingField.ORDER_DESC))),
                new QueryWrapper<ConferenceRoomEntity>()
                        .like(req.getCode() != null, "cr.code", req.getCode())
                        .like(req.getAddress() != null, "cr.address", req.getAddress())
                        .eq(req.getStatus() != null, "cr.status", req.getStatus())
                        .in(selectRoomIds != null, "cr.id", selectRoomIds)
        );
        return new PageResult<>(iPage.getRecords(), iPage.getTotal());
    }

    @Override
    public ConferenceRoomDTO getById(Long id) {
        ConferenceRoomEntity entity = checkConferenceRoomExists(id);
        ConferenceRoomDTO dto = new ConferenceRoomDTO();
        BeanUtil.copyProperties(entity, dto);
        dto.setConferenceEquipmentIds(
                conferenceRoomEquipmentMapper.selectList("conference_room_id", id)
                        .stream().map(ConferenceRoomEquipmentEntity::getConferenceEquipmentId)
                        .collect(Collectors.toSet())
        );
        return dto;
    }

    @Override
    public void add(ConferenceRoomDTO dto) {
        // ?????????????????????????????????
        checkRoomCodeUnique(dto.getCode());
        checkRoomAddressUnique(dto.getAddress());
        conferenceRoomMapper.insert(dto);
        if (CollUtil.isNotEmpty(dto.getConferenceEquipmentIds())) {
            // ???????????????????????????
            conferenceRoomEquipmentMapper.insertBatch(
                    dto.getConferenceEquipmentIds().stream()
                            .map(equipmentId -> new ConferenceRoomEquipmentEntity(null, dto.getId(), equipmentId))
                            .collect(Collectors.toList())
            );
            // ??????????????????????????????
            conferenceEquipmentMapper.update(
                    null,
                    Wrappers.<ConferenceEquipmentEntity>lambdaUpdate()
                            .set(ConferenceEquipmentEntity::getStatus, ConferenceEquipmentStatusEnum.USING.getStatus())
                            .in(ConferenceEquipmentEntity::getId, dto.getConferenceEquipmentIds())
            );
        }
    }

    @Override
    public void update(ConferenceRoomDTO dto) {
        ConferenceRoomEntity dbEntity = checkConferenceRoomExists(dto.getId());
        /* 4-24???????????????????????????????????????????????????????????????????????????roomCode?????????????????? */
        dto.setCode(null);
        // if (!dbEntity.getCode().equals(dto.getCode())) {
        //     // ??????????????????????????????????????????
        //     checkRoomCodeUnique(dto.getCode());
        // }
        if (!dbEntity.getAddress().equals(dto.getAddress())) {
            // ??????????????????????????????????????????
            checkRoomAddressUnique(dto.getAddress());
        }
        // ???????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????
        if (!dbEntity.getStatus().equals(dto.getStatus()) &&
                CommonStatusEnum.ENABLE.getStatus().equals(dbEntity.getStatus())) {
            AssertUtils.asserts(
                    !conferenceReservationMapper.checkStillNoneCompleteConferenceUnderRoom(dbEntity.getCode()),
                    ResultCode.CONFERENCE_ROOM_STILL_NONE_COMPLETE_CONFERENCE
            );
        }
        conferenceRoomMapper.updateById(dto);
        if (CollUtil.isNotEmpty(dto.getConferenceEquipmentIds())) {
            // ????????????????????????????????????????????????

            List<Long> dbEquipmentIds = conferenceRoomEquipmentMapper.selectList("conference_room_id", dto.getId())
                    .stream().map(ConferenceRoomEquipmentEntity::getConferenceEquipmentId)
                    .collect(Collectors.toList());
            // ??????????????????id
            Collection<Long> addEquipmentIds = CollUtil.subtract(dto.getConferenceEquipmentIds(), dbEquipmentIds);
            // ??????????????????id
            Collection<Long> removeEquipmentIds = CollUtil.subtract(dbEquipmentIds, dto.getConferenceEquipmentIds());
            // ??????
            if (CollUtil.isNotEmpty(removeEquipmentIds)) {
                conferenceRoomEquipmentMapper.delete(
                        new MyLambdaQueryWrapper<ConferenceRoomEquipmentEntity>()
                                .eq(ConferenceRoomEquipmentEntity::getConferenceRoomId, dto.getId())
                                .in(ConferenceRoomEquipmentEntity::getConferenceEquipmentId, removeEquipmentIds)
                );
                // ???????????????????????????????????????????????????
                conferenceEquipmentMapper.update(
                        null,
                        Wrappers.<ConferenceEquipmentEntity>lambdaUpdate()
                                .set(ConferenceEquipmentEntity::getStatus, ConferenceEquipmentStatusEnum.UNUSED.getStatus())
                                .in(ConferenceEquipmentEntity::getId, removeEquipmentIds)
                );
            }
            // ??????
            if (CollUtil.isNotEmpty(addEquipmentIds)) {
                conferenceRoomEquipmentMapper.insertBatch(
                        addEquipmentIds.stream()
                                .map(equipmentId -> new ConferenceRoomEquipmentEntity(null, dto.getId(), equipmentId))
                                .collect(Collectors.toList())
                );
                // ????????????????????????????????????????????????
                conferenceEquipmentMapper.update(
                        null,
                        Wrappers.<ConferenceEquipmentEntity>lambdaUpdate()
                                .set(ConferenceEquipmentEntity::getStatus, ConferenceEquipmentStatusEnum.USING.getStatus())
                                .in(ConferenceEquipmentEntity::getId, addEquipmentIds)
                );
            }
        }
    }

    @Override
    public void deleteById(Long id) {
        checkConferenceRoomExists(id);
        conferenceRoomMapper.deleteById(id);
        List<ConferenceRoomEquipmentEntity> relationEntities =
                conferenceRoomEquipmentMapper.selectList("conference_room_id", id);
        if (CollUtil.isNotEmpty(relationEntities)) {
            // ????????????????????????????????????
            conferenceRoomEquipmentMapper.deleteBatchIds(
                    relationEntities.stream().map(ConferenceRoomEquipmentEntity::getId).collect(Collectors.toSet())
            );
            // ?????????????????????????????????
            conferenceEquipmentMapper.update(
                    null,
                    Wrappers.<ConferenceEquipmentEntity>lambdaUpdate()
                            .set(ConferenceEquipmentEntity::getStatus, ConferenceEquipmentStatusEnum.UNUSED.getStatus())
                            .in(ConferenceEquipmentEntity::getId,
                                    relationEntities.stream()
                                            .map(ConferenceRoomEquipmentEntity::getConferenceEquipmentId)
                                            .collect(Collectors.toSet())
                            )
            );
        }
    }

    @Override
    public List<ConferenceRoomEntity> listAllConferenceRoom() {
        return conferenceRoomMapper.selectList(
                Wrappers.<ConferenceRoomEntity>lambdaQuery()
                        .select(ConferenceRoomEntity::getId, ConferenceRoomEntity::getCode)
                        .orderByDesc(ConferenceRoomEntity::getCreateTime)
        );
    }

    /**
     * ??????????????????????????????????????????
     * @param id ??????id
     * @return ???????????????
     */
    private ConferenceRoomEntity checkConferenceRoomExists(Long id) {
        ConferenceRoomEntity entity = conferenceRoomMapper.selectById(id);
        AssertUtils.asserts(entity != null, ResultCode.CONFERENCE_ROOM_NOT_FOUND);
        return entity;
    }

    /**
     * ?????????????????????????????????
     * @param code ??????
     */
    private void checkRoomCodeUnique(String code) {
        AssertUtils.asserts(conferenceRoomMapper.selectCount("code", code) == 0,
                ResultCode.CONFERENCE_ROOM_CODE_DUPLICATE);
    }

    /**
     * ?????????????????????????????????
     * @param address ??????
     */
    private void checkRoomAddressUnique(String address) {
        AssertUtils.asserts(conferenceRoomMapper.selectCount("address", address) == 0,
                ResultCode.CONFERENCE_ROOM_ADDRESS_DUPLICATE);
    }

}
