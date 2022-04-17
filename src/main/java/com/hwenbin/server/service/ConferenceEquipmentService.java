package com.hwenbin.server.service;

import com.hwenbin.server.controller.conferencecenter.req.PageQueryForConferenceEquipmentReq;
import com.hwenbin.server.core.web.response.PageResult;
import com.hwenbin.server.dto.ConferenceEquipmentDTO;
import com.hwenbin.server.dto.ConferenceEquipmentTreeNode;
import com.hwenbin.server.entity.ConferenceEquipmentEntity;

import java.util.List;

/**
 * @author hwb
 * @date 2022/04/13 16:50
 */
public interface ConferenceEquipmentService {

    /**
     * 分页查询
     * @param req 实体参数 + 分页参数
     * @return 分页结果
     */
    PageResult<ConferenceEquipmentEntity> pageQuery(PageQueryForConferenceEquipmentReq req);

    /**
     * 获取实体
     * @param id 实体id
     * @return 实体
     */
    ConferenceEquipmentDTO getById(Long id);

    /**
     * 新增
     * @param dto 实体dto
     */
    void add(ConferenceEquipmentDTO dto);

    /**
     * 更新
     * @param dto 实体dto
     */
    void update(ConferenceEquipmentDTO dto);

    /**
     * 删除
     * @param id 实体id
     */
    void deleteById(Long id);

    /**
     * 获取 按类型分类的树形结构数据列表
     * @return 数据列表
     * @param conferenceRoomId 更新表单需要传会议室id
     */
    List<ConferenceEquipmentTreeNode> groupByTypeList(Long conferenceRoomId);

    /**
     * 获取 所有设备类型（不重复）
     * @return 类型名称列表
     */
    List<String> listAllTypes();

}
