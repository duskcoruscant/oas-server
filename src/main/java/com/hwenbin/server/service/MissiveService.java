package com.hwenbin.server.service;

import com.hwenbin.server.controller.missivecenter.req.PageQueryForMissiveReq;
import com.hwenbin.server.core.web.response.PageResult;
import com.hwenbin.server.dto.MissiveDTO;
import com.hwenbin.server.entity.MissiveEntity;

/**
 * @author hwb
 * @date 2022/04/17 23:45
 */
public interface MissiveService {

    /**
     * 分页查询
     * @param req 实体参数 + 分页参数
     * @return 分页结果
     */
    PageResult<MissiveDTO> pageQuery(PageQueryForMissiveReq req);

    /**
     * 通过id获取实体
     * @param id 实体id
     * @return 实体
     */
    MissiveEntity getById(Long id);

    /**
     * 新增公文
     * @param entity 公文实体
     */
    void add(MissiveEntity entity);

    /**
     * 更新公文
     * @param entity 公文实体
     */
    void update(MissiveEntity entity);

    /**
     * 删除公文
     * @param id 流水号
     */
    void deleteById(Long id);

}
