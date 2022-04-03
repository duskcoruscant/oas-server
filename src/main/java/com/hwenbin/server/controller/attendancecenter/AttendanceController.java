package com.hwenbin.server.controller.attendancecenter;

import com.hwenbin.server.controller.attendancecenter.req.GetEmpAttendanceListReq;
import com.hwenbin.server.core.web.response.CommonResult;
import com.hwenbin.server.core.web.response.ResultGenerator;
import com.hwenbin.server.entity.Attendance;
import com.hwenbin.server.service.AttendanceService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.List;

/**
 * @author hwb
 * @create 2022-04-02
 */
@RestController
@RequestMapping("/attendance")
public class AttendanceController {

    @Resource
    private AttendanceService attendanceService;

    /**
     * 获取所有指定员工的特定时期的所有考勤记录
     * @param req 包含员工id与指定日期或日期范围
     * @return 员工的考勤记录List
     */
    @GetMapping("/getByMonthOrDate")
    public CommonResult<List<Attendance>> getEmpAttendsByMonthOrDate(@Valid final GetEmpAttendanceListReq req) {
        return ResultGenerator.genOkResult(
                attendanceService.getEmpAttendsByMonthOrDate(req.getEmpId(), req.getYear(), req.getMonth(), req.getDay())
        );
    }

    /**
     * 签到
     * @param empId 员工id
     * @return 考勤信息
     */
    @PostMapping
    public CommonResult<Attendance> clockIn(@RequestParam final Long empId) {
        return ResultGenerator.genOkResult(attendanceService.addAttendance(empId));
    }

    /**
     * 签退
     * @param empId 员工id
     * @return 考勤信息
     */
    @PutMapping
    public CommonResult<Attendance> clockOut(@RequestParam final Long empId) {
        return ResultGenerator.genOkResult(attendanceService.updateAttendance(empId));
    }

}
