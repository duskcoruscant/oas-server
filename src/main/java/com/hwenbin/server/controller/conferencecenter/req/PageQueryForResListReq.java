package com.hwenbin.server.controller.conferencecenter.req;

import com.hwenbin.server.core.constant.ProjectConstant;
import com.hwenbin.server.core.web.response.PageParam;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * @author hwb
 * @date 2022/04/21 21:42
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class PageQueryForResListReq extends PageParam {

    /**
     * 预订人id
     */
    private String resEmpId;

    /**
     * 会议室编号
     */
    private String roomCode;

    /**
     * 会议主题
     */
    private String subject;

    /**
     * 会议日期范围 —— 开始
     */
    @DateTimeFormat(pattern = ProjectConstant.FORMAT_YEAR_MONTH_DAY)
    private Date beginTime;

    /**
     * 会议日期范围 —— 结束
     */
    @DateTimeFormat(pattern = ProjectConstant.FORMAT_YEAR_MONTH_DAY)
    private Date endTime;

    /**
     * 会议状态
     */
    private Integer processStatus;

}
