package com.hwenbin.server.controller.missivecenter.req;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

/**
 * @author hwb
 * @date 2022/04/19 15:30
 */
@Data
public class UpdateMissiveReq {

    /**
     * 流水号
     */
    @NotNull(message = "公文流水号不能为空")
    private Long id;

    /**
     * 公文名称
     */
    @NotEmpty(message = "公文名称不能为空")
    private String name;

    /**
     * 公文类型（1通告、2指示、3议案、4决议、5命令）
     */
    @NotNull(message = "公文类型不能为空")
    private Integer type;

    /**
     * 机密程度（1公开，2部门）
     */
    @NotNull(message = "公文机密程度不能为空")
    private Integer secretLevel;

    /**
     * 主送部门id
     */
    private Long primarySendDeptId;

    /**
     * 抄送部门id
     */
    private Long copySendDeptId;

    /**
     * 拟稿人id
     */
    @NotNull(message = "拟稿人不能为空")
    private Long authorId;

    /**
     * 拟稿部门id
     */
    private Long authorDeptId;

    /**
     * 正文
     */
    @NotEmpty(message = "公文正文不能为空")
    private String content;

    /**
     * 附件id
     */
    private Long attachmentId;

}
