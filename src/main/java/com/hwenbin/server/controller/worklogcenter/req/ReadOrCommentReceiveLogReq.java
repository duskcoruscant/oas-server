package com.hwenbin.server.controller.worklogcenter.req;

import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @author hwb
 * @date 2022/04/24 17:45
 */
@Data
public class ReadOrCommentReceiveLogReq {

    /**
     * 实体id —— work_log_send 表id
     */
    @NotNull(message = "receive日志的id不能为空")
    private Long id;

    /**
     * 已读
     */
    private Boolean isRead;

    /**
     * 评论
     */
    private String comment;

}
