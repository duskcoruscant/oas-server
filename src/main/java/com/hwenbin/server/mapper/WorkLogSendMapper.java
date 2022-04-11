package com.hwenbin.server.mapper;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.hwenbin.server.core.mybatis.mapper.MyBaseMapper;
import com.hwenbin.server.dto.WorkLogSendDTO;
import com.hwenbin.server.entity.WorkLogSendEntity;
import org.apache.ibatis.annotations.Param;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author hwb
 * @date 2022/04/11 14:08
 */
public interface WorkLogSendMapper extends MyBaseMapper<WorkLogSendEntity> {

    /**
     * 新增日志-通知人关联记录
     * @param logId 日志id
     * @param sendEmpIds 通知人集合
     */
    default void insertByLogIdAndSendEmpIds(Long logId, Collection<Long> sendEmpIds) {
        List<WorkLogSendEntity> sendEntities = sendEmpIds.stream().map(empId -> {
            WorkLogSendEntity sendEntity = new WorkLogSendEntity();
            sendEntity.setLogId(logId);
            sendEntity.setSendEmpId(empId);
            return sendEntity;
        }).collect(Collectors.toList());
        this.insertBatch(sendEntities);
    }

    /**
     * 分页查询
     * @param page 分页参数
     * @param queryWrapper 查询条件
     * @return 分页及结果
     */
    IPage<WorkLogSendDTO> pageQuery(IPage<WorkLogSendDTO> page,
                                    @Param("ew") LambdaQueryWrapper<WorkLogSendEntity> queryWrapper);

}
