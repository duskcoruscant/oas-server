package com.hwenbin.server.controller.attendancecenter.req;

import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @author hwb
 * @create 2022-04-02
 */
@Data
public class GetEmpAttendanceListReq {

    /**
     * 员工id
     */
    @NotNull(message = "员工id不能为空")
    private Long empId;

    /**
     * 年份
     */
    @NotNull(message = "年份不能为空")
    private Integer year;

    /**
     * 月份
     */
    @NotNull(message = "月份不能为空")
    private Integer month;

    /**
     * 日
     */
    private Integer day;

}
