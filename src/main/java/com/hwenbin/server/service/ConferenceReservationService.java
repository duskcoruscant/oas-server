package com.hwenbin.server.service;

import com.hwenbin.server.controller.conferencecenter.req.PageQueryForResListReq;
import com.hwenbin.server.controller.conferencecenter.req.PageQueryForReservationReq;
import com.hwenbin.server.core.web.response.PageResult;
import com.hwenbin.server.dto.ConferenceReservationDTO;
import com.hwenbin.server.entity.ConferenceReservationEntity;

/**
 * @author hwb
 * @date 2022/04/20 22:44
 */
public interface ConferenceReservationService {

    /**
     * 分页查询 —— 会议预订
     * @param req 查询参数 + 分页参数
     * @return 分页列表
     */
    PageResult<ConferenceReservationDTO> pageQueryForReservation(PageQueryForReservationReq req);

    /**
     * 新增会议预订实体
     * @param entity 实体
     */
    void add(ConferenceReservationEntity entity);

    /**
     * 分页查询 —— 会议列表
     * @param req 查询参数 + 分页参数
     * @return 分页列表
     */
    PageResult<ConferenceReservationEntity> pageQueryForResHistory(PageQueryForResListReq req);

    /**
     * 删除会议记录（只允许删除未开始的会议）
     * @param id 实体id
     */
    void delete(Long id);

}
