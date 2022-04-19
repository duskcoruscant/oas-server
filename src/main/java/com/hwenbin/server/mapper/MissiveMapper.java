package com.hwenbin.server.mapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.hwenbin.server.core.mybatis.mapper.MyBaseMapper;
import com.hwenbin.server.dto.MissiveDTO;
import com.hwenbin.server.entity.MissiveEntity;
import org.apache.ibatis.annotations.Param;

/**
 * @author hwb
 * @date 2022/04/17 23:44
 */
public interface MissiveMapper extends MyBaseMapper<MissiveEntity> {

    /**
     * 分页查询
     * @param page 分页参数
     * @param queryWrapper 查询条件
     * @return 分页结果
     */
    IPage<MissiveDTO> pageQuery(IPage<MissiveDTO> page, @Param("ew") QueryWrapper<MissiveDTO> queryWrapper);

}
