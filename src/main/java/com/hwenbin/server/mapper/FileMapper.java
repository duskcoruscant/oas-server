package com.hwenbin.server.mapper;

import com.hwenbin.server.core.mybatis.mapper.MyBaseMapper;
import com.hwenbin.server.entity.File;
import org.apache.ibatis.annotations.Param;

import java.util.Collection;
import java.util.List;

/**
 * @author hwb
 * @create 2022-04-03
 */
public interface FileMapper extends MyBaseMapper<File> {

    /**
     * 根据删除批次号字段获取文件列表
     * @param deletedBatchNums 删除批次号列表
     * @return 文件列表
     */
    List<File> selectListByDeleteBatchNums(@Param("deletedBatchNums") Collection<String> deletedBatchNums);

    /**
     * 根据删除批次号恢复文件，即修改逻辑删除状态
     * @param deletedBatchNums 删除批次号列表
     */
    void updateDeleteStatusByDeleteBatchNums(@Param("deletedBatchNums") Collection<String> deletedBatchNums);

}
