package com.hwenbin.server.mapper;

import com.hwenbin.server.core.mybatis.mapper.MyBaseMapper;
import com.hwenbin.server.entity.ConferenceEquipmentEntity;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @author hwb
 * @date 2022/04/13 16:49
 */
public interface ConferenceEquipmentMapper extends MyBaseMapper<ConferenceEquipmentEntity> {

    /**
     * 获取 所有设备类型（不重复）
     * @return 设备类型列表
     */
    @Select("SELECT type FROM conference_equipment GROUP BY type")
    List<String> listAllTypes();

}
