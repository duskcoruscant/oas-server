package com.hwenbin.server.controller.managecenter.req;

import com.hwenbin.server.core.constant.ProjectConstant;
import com.hwenbin.server.core.web.response.PageParam;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * @author hwb
 * @create 2022-03-27
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class GetEmployeeListReq extends PageParam {

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
