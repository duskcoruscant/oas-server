package com.hwenbin.server.mapper;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.hwenbin.server.core.mybatis.mapper.MyBaseMapper;
import com.hwenbin.server.dto.ConferenceReservationDTO;
import com.hwenbin.server.entity.ConferenceReservationEntity;
import org.apache.ibatis.annotations.Param;

import java.util.Date;

/**
 * @author hwb
 * @date 2022/04/20 22:43
 */
public interface ConferenceReservationMapper extends MyBaseMapper<ConferenceReservationEntity> {

    /**
     * 分页查询 —— 会议室预订
     * @param resDate 预订日期
     * @param page 分页参数
     * @param queryWrapper 查询条件
     * @return 分页结果
     */
    IPage<ConferenceReservationDTO> pageQueryForReservation(
            Date resDate, IPage<ConferenceReservationDTO> page,
            @Param("ew") QueryWrapper<ConferenceReservationDTO> queryWrapper);

    /**
     * 分页查询 —— 会议列表
     * @param processStatus 会议状态
     * @param page 分页参数
     * @param queryWrapper 查询条件
     * @return 分页结果
     */
    IPage<ConferenceReservationEntity> pageQueryForResHistory(
            Integer processStatus, IPage<ConferenceReservationEntity> page,
            @Param("ew") LambdaQueryWrapper<ConferenceReservationEntity> queryWrapper);

    /**
     * 检查roomCode会议室下是否还有未结束的会议
     * @param roomCode 会议室编号
     * @return 存在则返回true，否则返回false
     */
    Boolean checkStillNoneCompleteConferenceUnderRoom(String roomCode);

}
