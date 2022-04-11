package com.hwenbin.server.controller.worklogcenter.req;

import com.hwenbin.server.core.constant.ProjectConstant;
import com.hwenbin.server.core.web.response.PageParam;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * @author hwb
 * @date 2022/04/11 15:00
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class PageQueryForReceiveLogReq extends PageParam {

    /**
     * 查询人id
     */
    private Long empId;

    /**
     * 日志类型
     */
    private Integer type;

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
