package com.hwenbin.server.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hwenbin.server.controller.managecenter.req.GetPositionListReq;
import com.hwenbin.server.core.web.response.PageResult;
import com.hwenbin.server.dto.PositionDTO;
import com.hwenbin.server.entity.Position;

import java.util.Collection;
import java.util.Map;

/**
 * @author hwb
 * @create 2022-03-28
 */
public interface PositionService extends IService<Position> {

    /**
     * 分页查询
     * @param req 分页 + 筛选条件
     * @return 分页结果
     */
    PageResult<Position> pageQuery(GetPositionListReq req);

    /**
     * 新增职位
     * @param positionDTO 新增的职位信息
     */
    void add(PositionDTO positionDTO);

    /**
     * 更新职位信息
     * @param positionDTO 更新的职位信息
     */
    void update(PositionDTO positionDTO);

    /**
     * 获取职位信息
     * @param id 职位id
     * @return 职位信息
     */
    Position getDetail(Long id);

    /**
     * 删除职位
     * @param id 职位id
     */
    void delete(Long id);

    /**
     * 根据id列表获取职位map
     * @param positionIds 职位id列表
     * @return key = 职位id，value = 职位名称
     */
    Map<Long, String> getPositionMapByIds(Collection<Long> positionIds);

}
