package com.hwenbin.server.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hwenbin.server.entity.Attendance;

import java.util.List;

/**
 * @author hwb
 * @create 2022-04-02
 */
public interface AttendanceService extends IService<Attendance> {

    /**
     * 获取所有指定员工的特定时期的所有考勤记录
     * @param empId 员工id
     * @param year 年
     * @param month 月
     * @param day 日 为空时查询的是月份考勤记录
     * @return 员工考勤列表
     */
    List<Attendance> getEmpAttendsByMonthOrDate(Long empId, Integer year, Integer month, Integer day);

    /**
     * 新增考勤记录（签到）
     * @param empId 员工id
     * @return 如果迟到，返回迟到分钟数
     */
    Attendance addAttendance(Long empId);

    /**
     * 更新考勤记录（签退）
     * @param empId 员工id
     * @return 如果早退，返回早退分钟数
     */
    Attendance updateAttendance(Long empId);

}
