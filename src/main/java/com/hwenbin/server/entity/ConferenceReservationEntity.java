package com.hwenbin.server.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDate;
import java.util.Date;

/**
 * @author hwb
 * @date 2022/04/20 22:37
 */
@Data
@TableName("conference_reservation")
public class ConferenceReservationEntity {

    /**
     * 自增id
     */
    @TableId
    private Long id;

    /**
     * 会议室编号
     */
    private String roomCode;

    /**
     * 会议预订日期
     */
    private LocalDate date;

    /**
     * 会议开始时间 hh(/h):mm
     */
    private String startTime;

    /**
     * 会议结束时间 hh(/h):mm
     */
    private String endTime;

    /**
     * 预订人id
     */
    private Long resEmpId;

    /**
     * 会议主题
     */
    private String subject;

    /**
     * 创建人
     */
    private String creator;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 逻辑删除字段
     */
    @TableLogic
    private Boolean isDeleted;

    /**
     * 预订人姓名 —— @TableField(exist = false)表示实体类中有的属性但是数据表中没有的字段
     * 数据库中没有，联表查询注入
     */
    @TableField(exist = false)
    private String resEmpName;

    /**
     * 会议状态（1未开始，2进行中，3已结束） —— @TableField(exist = false)表示实体类中有的属性但是数据表中没有的字段
     * 数据库中没有，分页查询时进行代码计算
     */
    @TableField(exist = false)
    private Integer processStatus;

}
