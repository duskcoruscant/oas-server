package com.hwenbin.server.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

/**
 * 工作日志实体类
 *
 * @author hwb
 * @date 2022/04/10 20:50
 */
@Data
@TableName(value = "work_log", autoResultMap = true)
public class WorkLogEntity {

    /**
     * 自增id
     */
    @TableId
    private Long id;

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
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

}
