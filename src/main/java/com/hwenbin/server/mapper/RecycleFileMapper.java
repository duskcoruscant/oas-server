package com.hwenbin.server.mapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.hwenbin.server.core.mybatis.mapper.MyBaseMapper;
import com.hwenbin.server.dto.RecycleFileDTO;
import com.hwenbin.server.entity.RecycleFile;
import org.apache.ibatis.annotations.Param;

/**
 * @author hwb
 * @date 2022/04/07 18:16
 */
public interface RecycleFileMapper extends MyBaseMapper<RecycleFile> {

    /**
     * 分页查询
     * @param page mybatisPlus 原生分页查询，查询SQL会自动拼接分页
     * @param queryWrapper QueryWrapper条件，注意，这里需要使用 @Param("ew") 指定mybatis参数
     * @return 分页结果
     */
    IPage<RecycleFileDTO> pageQuery(IPage<RecycleFile> page, @Param("ew") QueryWrapper<RecycleFile> queryWrapper);

}
