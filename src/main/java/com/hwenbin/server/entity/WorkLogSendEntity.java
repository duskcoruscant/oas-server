package com.hwenbin.server.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

/**
 * @author hwb
 * @date 2022/04/11 14:06
 */
@Data
@TableName("work_log_send")
public class WorkLogSendEntity {

    /**
     * 自增id
     */
    @TableId
    private Long id;

    /**
     * 日志id
     */
    private Long logId;

    /**
     * 发送人id
     */
    private Long sendEmpId;

    /**
     * 是否已读
     */
    private Boolean isRead;

    /**
     * 评论
     */
    private String comment;

    /**
     * 评论时间
     */
    private Date commentTime;

}
