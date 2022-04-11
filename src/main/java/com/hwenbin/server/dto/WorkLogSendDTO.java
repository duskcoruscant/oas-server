package com.hwenbin.server.dto;

import lombok.Data;

import java.util.Date;

/**
 * @author hwb
 * @date 2022/04/11 14:33
 */
@Data
public class WorkLogSendDTO {

    /**
     * 自增id
     */
    private Long id;

    /**
     * 日志id
     */
    private Long logId;

    /**
     * 通知人id
     */
    private Long sendEmpId;

    /**
     * 日志类型（1日报，2周报，3月报）
     */
    private Integer type;

    /**
     * 日志标题
     */
    private String title;

    /**
     * 今日工作内容
     */
    private String todayContent;

    /**
     * 明日工作内容
     */
    private String tomorrowContent;

    /**
     * 遇到的问题
     */
    private String question;

    /**
     * 创建人id
     */
    private Long createEmpId;

    /**
     * 创建人姓名
     */
    private String createEmpName;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

}
