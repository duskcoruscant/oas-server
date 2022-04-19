package com.hwenbin.server.dto;

import com.hwenbin.server.entity.MissiveEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author hwb
 * @date 2022/04/18 13:38
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class MissiveDTO extends MissiveEntity {

    /**
     * 主送部门名称
     */
    private String primarySendDeptName;

    /**
     * 抄送部门名称
     */
    private String copySendDeptName;

    /**
     * 拟稿人名称
     */
    private String authorName;

    /**
     * 拟稿部门名称
     */
    private String authorDeptName;

    /**
     * 附件名称
     */
    private String attachmentName;

}
