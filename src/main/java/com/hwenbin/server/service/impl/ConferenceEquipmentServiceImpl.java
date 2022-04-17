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
        // 如果使用状态为使用中，需要关联会议室
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
        // 验证编号的的唯一性
        checkEquipmentCodeUnique(dto.getCode());
        conferenceEquipmentMapper.insert(dto);
        // 如果使用状态为使用中，需要关联会议室
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
            // 更改编号，需要验证唯一性
            checkEquipmentCodeUnique(dto.getCode());
        }
        conferenceEquipmentMapper.updateById(dto);
        // 关联会议室操作分情况
        if (ConferenceEquipmentStatusEnum.USING.getStatus().equals(dbEntity.getStatus())) {
            if (dto.getStatus().equals(dbEntity.getStatus())) {
                // 使用中 -> 使用中
                conferenceRoomEquipmentMapper.update(null,
                        Wrappers.<ConferenceRoomEquipmentEntity>lambdaUpdate()
                                .set(ConferenceRoomEquipmentEntity::getConferenceRoomId, dto.getRelatedRoomId())
                                .eq(ConferenceRoomEquipmentEntity::getConferenceEquipmentId, dto.getId())
                );
            } else {
                // 使用中 -> 非使用中
                conferenceRoomEquipmentMapper.deleteByMap(ImmutableMap.of("conference_equipment_id", dto.getId()));
            }
        } else if (ConferenceEquipmentStatusEnum.USING.getStatus().equals(dto.getStatus())) {
            // 非使用中 -> 使用中
            conferenceRoomEquipmentMapper.insert(
                    new ConferenceRoomEquipmentEntity(null, dto.getRelatedRoomId(), dto.getId())
            );
        }
    }

    @Override
    public void deleteById(Long id) {
        ConferenceEquipmentEntity entity = checkEquipmentExists(id);
        // 设备使用中，不允许删除
        AssertUtils.asserts(!ConferenceEquipmentStatusEnum.USING.getStatus().equals(entity.getStatus()),
                ResultCode.CONFERENCE_EQUIPMENT_USING_NOT_PERMIT_DELETE);
        conferenceEquipmentMapper.deleteById(id);
    }

    @Override
    public List<ConferenceEquipmentTreeNode> groupByTypeList(Long conferenceRoomId) {
        Map<String, List<ConferenceEquipmentEntity>> typeListMap =
                conferenceEquipmentMapper.selectList().stream()
                        .collect(Collectors.groupingBy(ConferenceEquipmentEntity::getType));
        // 如果为更新表单，那么当前会议室选过的不应该被置为disabled
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
     * 验证 会议室设备 是否存在
     * @param id 实体id
     * @return 实体
     */
    private ConferenceEquipmentEntity checkEquipmentExists(Long id) {
        ConferenceEquipmentEntity entity = conferenceEquipmentMapper.selectById(id);
        AssertUtils.asserts(entity != null, ResultCode.CONFERENCE_EQUIPMENT_NOT_FOUND);
        return entity;
    }

    /**
     * 验证 会议室设备编号 的唯一性
     * @param code 设备编号
     */
    private void checkEquipmentCodeUnique(String code) {
        AssertUtils.asserts(conferenceEquipmentMapper.selectCount("code", code) == 0,
                ResultCode.CONFERENCE_EQUIPMENT_CODE_DUPLICATE);
    }

}
