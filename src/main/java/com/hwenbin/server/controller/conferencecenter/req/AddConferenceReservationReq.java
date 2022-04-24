package com.hwenbin.server.controller.conferencecenter.req;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * @author hwb
 * @date 2022/04/21 19:34
 */
@Data
public class AddConferenceReservationReq {

    /**
     * 会议室编号
     */
    @NotEmpty(message = "会议室编号不能为空")
    private String roomCode;

    /**
     * 会议预订日期
     */
    @NotNull(message = "会议预订日期不能为空")
    private Date date;

    /**
     * 会议开始时间 hh(/h):mm
     */
    @NotEmpty(message = "会议开始时间不能为空")
    private String startTime;

    /**
     * 会议结束时间 hh(/h):mm
     */
    @NotEmpty(message = "会议结束时间不能为空")
    private String endTime;

    /**
     * 预订人id
     */
    @NotNull(message = "预订人不能为空")
    private Long resEmpId;

    /**
     * 会议主题
     */
    @NotEmpty(message = "会议主题不能为空")
    private String subject;

}
