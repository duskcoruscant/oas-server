package com.hwenbin.server.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.hwenbin.server.core.mybatis.mapper.MyBaseMapper;
import com.hwenbin.server.entity.WorkflowFormEntity;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 流程表单Mapper接口
 *
 * @author hwb
 * @date 2022/04/29 18:52
 */
public interface WorkflowFormMapper extends MyBaseMapper<WorkflowFormEntity> {

    /**
     * 查询表单列表
     * @param queryWrapper 查询条件
     * @return 流程表单实体列表
     */
    List<WorkflowFormEntity> selectFormList(@Param(Constants.WRAPPER) Wrapper<WorkflowFormEntity> queryWrapper);

}
