package com.hwenbin.server.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hwenbin.server.core.web.response.ResultCode;
import com.hwenbin.server.entity.Attendance;
import com.hwenbin.server.mapper.AttendanceMapper;
import com.hwenbin.server.service.AttendanceService;
import com.hwenbin.server.util.AssertUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.time.*;
import java.util.Date;
import java.util.List;

/**
 * @author hwb
 * @create 2022-04-02
 */
@Service
@Transactional(rollbackFor = Exception.class)
@PropertySource("classpath:config/attendance-time-setting.properties")
public class AttendanceServiceImpl extends ServiceImpl<AttendanceMapper, Attendance> implements AttendanceService {

    @Resource
    private AttendanceMapper attendanceMapper;

    @Value("${attendance.working_hours_start}")
    private String workingHoursStart;

    @Value("${attendance.working_hours_end}")
    private String workingHoursEnd;

    @Override
    public List<Attendance> getEmpAttendsByMonthOrDate(Long empId, Integer year, Integer month, Integer day) {
        return attendanceMapper.listByEmpIdAndClockDate(empId, year, month, day);
    }

    @Override
    public Attendance addAttendance(Long empId) {
        // 校验该员工今日是否签到过
        AssertUtils.asserts(
                attendanceMapper.selectOne("emp_id", empId, "clock_date", LocalDate.now()) == null,
                ResultCode.EMP_TODAY_HAS_CLOCKED_IN
        );
        // 今日未签到
        Date date = new Date();
        long comeLateMinutes =
                Duration.between(LocalTime.parse(workingHoursStart), convertDateToLocalTime(date)).toMinutes();
        // 查询每日上班时间
        final Attendance attendance = new Attendance();
        attendance.setEmpId(empId);
        attendance.setClockDate(date);
        attendance.setClockInTime(date);
        if (comeLateMinutes > 0) {
            // 迟到
            attendance.setComeLateMinutes((int) comeLateMinutes);
        }
        attendanceMapper.insert(attendance);
        return attendance;
    }

    @Override
    public Attendance updateAttendance(Long empId) {
        Attendance attendance =
                attendanceMapper.selectOne("emp_id", empId, "clock_date", LocalDate.now());
        // 校验该员工今日是否签到过
        AssertUtils.asserts(attendance != null, ResultCode.EMP_TODAY_HAS_NOT_CLOCKED_IN);
        // 校验该员工今日是否签退过（这里已经判断过attendance不为null，忽略代码提示）
        AssertUtils.asserts(attendance.getClockOutTime() == null, ResultCode.EMP_TODAY_HAS_CLOCKED_OUT);
        // 已签到、未签退
        Date date = new Date();
        long leaveEarlyMinutes =
                Duration.between(convertDateToLocalTime(date), LocalTime.parse(workingHoursEnd)).toMinutes();
        final Attendance updateA = new Attendance();
        updateA.setId(attendance.getId());
        updateA.setClockOutTime(date);
        if (leaveEarlyMinutes > 0) {
            updateA.setLeaveEarlyMinutes((int) leaveEarlyMinutes);
        }
        attendanceMapper.updateById(updateA);
        return updateA;
    }

    // 将Date转换为LocalTime类型
    private LocalTime convertDateToLocalTime(Date date) {
        return LocalDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault()).toLocalTime();
    }

}
