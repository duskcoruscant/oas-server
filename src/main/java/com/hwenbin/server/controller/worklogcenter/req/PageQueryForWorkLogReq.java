package com.hwenbin.server.controller.worklogcenter.req;

import com.hwenbin.server.core.constant.ProjectConstant;
import com.hwenbin.server.core.web.response.PageParam;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * @author hwb
 * @date 2022/04/10 21:25
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class PageQueryForWorkLogReq extends PageParam {

    /**
     * 日志创建人id
     */
    private Long empId;

    /**
     * 日志类型
     */
    private Integer type;

    /**
     * 日志标题
     */
    private String title;

    /**
     * 开始时间
     */
    @DateTimeFormat(pattern = ProjectConstant.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private Date beginTime;

    /**
     * 结束时间
     */
    @DateTimeFormat(pattern = ProjectConstant.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private Date endTime;

}
