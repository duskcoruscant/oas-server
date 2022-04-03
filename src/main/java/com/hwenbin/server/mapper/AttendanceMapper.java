package com.hwenbin.server.mapper;

import com.hwenbin.server.core.mybatis.mapper.MyBaseMapper;
import com.hwenbin.server.entity.Attendance;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author hwb
 * @create 2022-04-02
 */
public interface AttendanceMapper extends MyBaseMapper<Attendance> {

    /**
     * 通过员工id和日期查询所有考勤记录
     *
     * @param empId 员工id
     * @param year   年
     * @param month  月
     * @param day    日
     * @return 考勤记录列表
     */
    List<Attendance> listByEmpIdAndClockDate(@Param("empId") Long empId, @Param("year") Integer year,
                                              @Param("month") Integer month, @Param("day") Integer day);

}
