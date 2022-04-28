package com.hwenbin.server.mapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.hwenbin.server.core.mybatis.mapper.MyBaseMapper;
import com.hwenbin.server.dto.WorkLogDTO;
import com.hwenbin.server.entity.WorkLogEntity;
import org.apache.ibatis.annotations.Param;

/**
 * @author hwb
 * @date 2022/04/10 21:16
 */
public interface WorkLogMapper extends MyBaseMapper<WorkLogEntity> {

    /**
     * 分页查询
     * @param page 分页参数
     * @param queryWrapper 查询条件
     * @return 分页结果
     */
    IPage<WorkLogDTO> pageQuery(
            IPage<WorkLogEntity> page, @Param("ew") QueryWrapper<WorkLogEntity> queryWrapper);

    /**
     * 获取日志详情及其评论列表
     * @param id 日志实体id
     * @return dto（与实体相比多了comments）
     */
    WorkLogDTO getWorkLogWithCommentsById(Long id);

    /**
     * 获取日志详情及其 通知人ids
     * @param id 日志实体id
     * @return dto（与实体相比多了sendEmpIds）
     */
    WorkLogDTO getWorkLogWithSendEmpIdsById(Long id);

}
