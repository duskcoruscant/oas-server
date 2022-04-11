package com.hwenbin.server.controller.worklogcenter.req;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.Set;

/**
 * @author hwb
 * @date 2022/04/10 22:42
 */
@Data
public class AddWorkLogReq {

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
     * 日报标题
     */
    private String title;

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
     * 创建人id
     */
    @NotNull(message = "创建人不能为空")
    private Long createEmpId;

    /**
     * 创建时间
     */
    private Date createTime = new Date();

}
