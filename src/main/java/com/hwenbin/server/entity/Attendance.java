package com.hwenbin.server.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

/**
 * @author hwb
 * @create 2022-04-02
 */
@TableName("attendance")
@Data
public class Attendance {

    /**
     * 自增id
     */
    @TableId
    private Long id;

    /**
     * 员工id
     */
    private Long empId;

    /**
     * 打卡日期
     */
    private Date clockDate;

    /**
     * 签到时间
     */
    private Date clockInTime;

    /**
     * 签退时间
     */
    private Date clockOutTime;

    /**
     * 迟到分钟数
     */
    private Integer comeLateMinutes;

    /**
     * 早退分钟数
     */
    private Integer leaveEarlyMinutes;

    /**
     * 更新者
     */
    private String updater;

    /**
     * 更新时间
     */
    private Date updateTime;

}
