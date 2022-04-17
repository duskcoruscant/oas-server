package com.hwenbin.server.service;

import com.hwenbin.server.controller.conferencecenter.req.PageQueryForConferenceRoomReq;
import com.hwenbin.server.core.web.response.PageResult;
import com.hwenbin.server.dto.ConferenceRoomDTO;
import com.hwenbin.server.entity.ConferenceRoomEntity;

import java.util.List;

/**
 * @author hwb
 * @date 2022/04/14 13:38
 */
public interface ConferenceRoomService {

    /**
     * 分页查询
     * @param req 实体参数 + 分页参数
     * @return 分页结果
     */
    PageResult<ConferenceRoomDTO> pageQuery(PageQueryForConferenceRoomReq req);

    /**
     * 获取实体
     * @param id 实体id
     * @return dto
     */
    ConferenceRoomDTO getById(Long id);

    /**
     * 新增
     * @param dto 实体dto
     */
    void add(ConferenceRoomDTO dto);

    /**
     * 更新
     * @param dto 实体dto
     */
    void update(ConferenceRoomDTO dto);

    /**
     * 删除
     * @param id 实体id
     */
    void deleteById(Long id);

    /**
     * 获取所有
     * @return 实体列表
     */
    List<ConferenceRoomEntity> listAllConferenceRoom();

}
