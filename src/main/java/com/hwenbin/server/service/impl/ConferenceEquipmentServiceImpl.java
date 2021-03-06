package com.hwenbin.server.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.ListUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.google.common.collect.ImmutableMap;
import com.hwenbin.server.controller.conferencecenter.req.PageQueryForConferenceEquipmentReq;
import com.hwenbin.server.core.mybatis.query.MyLambdaQueryWrapper;
import com.hwenbin.server.core.web.response.PageResult;
import com.hwenbin.server.core.web.response.ResultCode;
import com.hwenbin.server.core.web.response.SortingField;
import com.hwenbin.server.dto.ConferenceEquipmentDTO;
import com.hwenbin.server.dto.ConferenceEquipmentTreeNode;
import com.hwenbin.server.entity.ConferenceEquipmentEntity;
import com.hwenbin.server.entity.ConferenceRoomEquipmentEntity;
import com.hwenbin.server.enums.ConferenceEquipmentStatusEnum;
import com.hwenbin.server.mapper.ConferenceEquipmentMapper;
import com.hwenbin.server.mapper.ConferenceRoomEquipmentMapper;
import com.hwenbin.server.service.ConferenceEquipmentService;
import com.hwenbin.server.util.AssertUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author hwb
 * @date 2022/04/13 16:51
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class ConferenceEquipmentServiceImpl implements ConferenceEquipmentService {

    @Resource
    private ConferenceEquipmentMapper conferenceEquipmentMapper;

    @Resource
    private ConferenceRoomEquipmentMapper conferenceRoomEquipmentMapper;

    @Override
    public PageResult<ConferenceEquipmentEntity> pageQuery(PageQueryForConferenceEquipmentReq req) {
        return conferenceEquipmentMapper.selectPage(
                req, ListUtil.of(new SortingField("create_time", SortingField.ORDER_DESC)),
                new MyLambdaQueryWrapper<ConferenceEquipmentEntity>()
                        .likeIfPresent(ConferenceEquipmentEntity::getCode, req.getCode())
                        .likeIfPresent(ConferenceEquipmentEntity::getType, req.getType())
                        .eqIfPresent(ConferenceEquipmentEntity::getStatus, req.getStatus())
        );
    }

    @Override
    public ConferenceEquipmentDTO getById(Long id) {
        ConferenceEquipmentEntity entity = checkEquipmentExists(id);
        ConferenceEquipmentDTO dto = new ConferenceEquipmentDTO();
        BeanUtil.copyProperties(entity, dto);
        // ??????????????????????????????????????????????????????
        if (ConferenceEquipmentStatusEnum.USING.getStatus().equals(dto.getStatus())) {
            dto.setRelatedRoomId(
                    conferenceRoomEquipmentMapper.selectOne(
                            ConferenceRoomEquipmentEntity::getConferenceEquipmentId, dto.getId()
                    ).getConferenceRoomId()
            );
        }
        return dto;
    }

    @Override
    public void add(ConferenceEquipmentDTO dto) {
        // ???????????????????????????
        checkEquipmentCodeUnique(dto.getCode());
        conferenceEquipmentMapper.insert(dto);
        // ??????????????????????????????????????????????????????
        if (ConferenceEquipmentStatusEnum.USING.getStatus().equals(dto.getStatus())) {
            conferenceRoomEquipmentMapper.insert(
                    new ConferenceRoomEquipmentEntity(null, dto.getRelatedRoomId(), dto.getId())
            );
        }
    }

    @Override
    public void update(ConferenceEquipmentDTO dto) {
        ConferenceEquipmentEntity dbEntity = checkEquipmentExists(dto.getId());
        if (!dbEntity.getCode().equals(dto.getCode())) {
            // ????????????????????????????????????
            checkEquipmentCodeUnique(dto.getCode());
        }
        conferenceEquipmentMapper.updateById(dto);
        // ??????????????????????????????
        if (ConferenceEquipmentStatusEnum.USING.getStatus().equals(dbEntity.getStatus())) {
            if (dto.getStatus().equals(dbEntity.getStatus())) {
                // ????????? -> ?????????
                conferenceRoomEquipmentMapper.update(null,
                        Wrappers.<ConferenceRoomEquipmentEntity>lambdaUpdate()
                                .set(ConferenceRoomEquipmentEntity::getConferenceRoomId, dto.getRelatedRoomId())
                                .eq(ConferenceRoomEquipmentEntity::getConferenceEquipmentId, dto.getId())
                );
            } else {
                // ????????? -> ????????????
                conferenceRoomEquipmentMapper.deleteByMap(ImmutableMap.of("conference_equipment_id", dto.getId()));
            }
        } else if (ConferenceEquipmentStatusEnum.USING.getStatus().equals(dto.getStatus())) {
            // ???????????? -> ?????????
            conferenceRoomEquipmentMapper.insert(
                    new ConferenceRoomEquipmentEntity(null, dto.getRelatedRoomId(), dto.getId())
            );
        }
    }

    @Override
    public void deleteById(Long id) {
        ConferenceEquipmentEntity entity = checkEquipmentExists(id);
        // ?????????????????????????????????
        AssertUtils.asserts(!ConferenceEquipmentStatusEnum.USING.getStatus().equals(entity.getStatus()),
                ResultCode.CONFERENCE_EQUIPMENT_USING_NOT_PERMIT_DELETE);
        conferenceEquipmentMapper.deleteById(id);
    }

    @Override
    public List<ConferenceEquipmentTreeNode> groupByTypeList(Long conferenceRoomId) {
        Map<String, List<ConferenceEquipmentEntity>> typeListMap =
                conferenceEquipmentMapper.selectList().stream()
                        .collect(Collectors.groupingBy(ConferenceEquipmentEntity::getType));
        // ????????????????????????????????????????????????????????????????????????disabled
        final List<Long> haveEquipmentIds;
        if (conferenceRoomId != null) {
            haveEquipmentIds =
                    conferenceRoomEquipmentMapper.selectList("conference_room_id", conferenceRoomId)
                            .stream()
                            .map(ConferenceRoomEquipmentEntity::getConferenceEquipmentId)
                            .collect(Collectors.toList());
        } else {
            haveEquipmentIds = new ArrayList<>();
        }
        List<ConferenceEquipmentTreeNode> result = new ArrayList<>();
        for (Map.Entry<String, List<ConferenceEquipmentEntity>> entry : typeListMap.entrySet()) {
            ConferenceEquipmentTreeNode parentTreeNode = new ConferenceEquipmentTreeNode();
            parentTreeNode.setLabel(entry.getKey());
            parentTreeNode.setChildren(
                    entry.getValue().stream().map(entity -> {
                        ConferenceEquipmentTreeNode node = new ConferenceEquipmentTreeNode();
                        node.setValue(entity.getId());
                        node.setLabel(entity.getCode());
                        if (!ConferenceEquipmentStatusEnum.UNUSED.getStatus().equals(entity.getStatus())
                                && !haveEquipmentIds.contains(entity.getId())) {
                            node.setDisabled(true);
                        }
                        return node;
                    }).collect(Collectors.toList())
            );
            result.add(parentTreeNode);
        }
        return result;
    }

    @Override
    public List<String> listAllTypes() {
        return conferenceEquipmentMapper.listAllTypes();
    }

    /**
     * ?????? ??????????????? ????????????
     * @param id ??????id
     * @return ??????
     */
    private ConferenceEquipmentEntity checkEquipmentExists(Long id) {
        ConferenceEquipmentEntity entity = conferenceEquipmentMapper.selectById(id);
        AssertUtils.asserts(entity != null, ResultCode.CONFERENCE_EQUIPMENT_NOT_FOUND);
        return entity;
    }

    /**
     * ?????? ????????????????????? ????????????
     * @param code ????????????
     */
    private void checkEquipmentCodeUnique(String code) {
        AssertUtils.asserts(conferenceEquipmentMapper.selectCount("code", code) == 0,
                ResultCode.CONFERENCE_EQUIPMENT_CODE_DUPLICATE);
    }

}
