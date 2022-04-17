package com.hwenbin.server.mapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.hwenbin.server.core.mybatis.mapper.MyBaseMapper;
import com.hwenbin.server.dto.ConferenceRoomDTO;
import com.hwenbin.server.entity.ConferenceRoomEntity;
import org.apache.ibatis.annotations.Param;

/**
 * @author hwb
 * @date 2022/04/14 13:37
 */
public interface ConferenceRoomMapper extends MyBaseMapper<ConferenceRoomEntity> {

    /**
     * 分页查询
     * @param page 分页参数
     * @param queryWrapper 查询条件
     * @return 分页及结果
     */
    IPage<ConferenceRoomDTO> pageQuery(IPage<ConferenceRoomDTO> page,
                                       @Param("ew") QueryWrapper<ConferenceRoomEntity> queryWrapper);
                                       // @Param("ew") LambdaQueryWrapper<ConferenceRoomEntity> queryWrapper);

}
