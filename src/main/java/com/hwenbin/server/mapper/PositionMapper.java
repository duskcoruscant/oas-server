package com.hwenbin.server.mapper;

import com.hwenbin.server.core.mybatis.mapper.MyBaseMapper;
import com.hwenbin.server.entity.Position;
import org.apache.ibatis.annotations.Param;

/**
 * @author hwb
 * @create 2022-03-28
 */
public interface PositionMapper extends MyBaseMapper<Position> {

    /**
     * 校验职位是否存在关联的员工
     * @param positionId 职位id
     * @return 存在则返回true，否则返回false
     */
    Boolean existRelatedEmployee(@Param("positionId") Long positionId);

}
