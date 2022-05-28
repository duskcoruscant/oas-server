package com.hwenbin.server.controller.managecenter.req;

import com.hwenbin.server.core.constant.ProjectConstant;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * 员工导出请求参数，与 GetEmployeeListReq 一致
 *
 * @author hwb
 * @date 2022/05/24 23:46
 */
@Data
public class ExportEmployeesReq {

    /**
     * 员工姓名
     */
    private String name;

    /**
     * 员工手机号码
     */
    private String phone;

    /**
     * 状态
     */
    private Integer status;

    /**
     * 入职日期 范围 - 开始
     */
    @DateTimeFormat(pattern = ProjectConstant.FORMAT_YEAR_MONTH_DAY)
    private Date beginTime;

    /**
     * 入职日期 范围 - 结束
     */
    @DateTimeFormat(pattern = ProjectConstant.FORMAT_YEAR_MONTH_DAY)
    private Date endTime;

    /**
     * 部门id
     */
    private Long deptId;

}
