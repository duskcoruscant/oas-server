package com.hwenbin.server.controller.worklogcenter.req;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.Set;

/**
 * @author hwb
 * @date 2022/04/10 23:40
 */
@Data
public class UpdateWorkLogReq {

    /**
     * 日志id
     */
    @NotNull(message = "日志id不能为空")
    private Long id;

    /**
     * 日报标题
     */
    private String title;

    /**
     * 日志类型 日报 / 周报 / 月报
     */
    @NotNull(message = "日志类型不能为空")
    private Integer type;

    /**
     * 通知人
     */
    private Set<Long> sendEmpIds;

    /**
     * 今日 / 本周 / 本月 工作内容
     */
    private String todayContent;

    /**
     * 明日 / 下周 / 下月 工作内容
     */
    private String tomorrowContent;

    /**
     * 问题
     */
    private String question;

    /**
     * 更新时间
     */
    private Date updateTime = new Date();

}
