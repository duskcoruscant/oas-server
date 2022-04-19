package com.hwenbin.server.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.hwenbin.server.core.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 公文实体
 *
 * @author hwb
 * @date 2022/04/17 23:43
 */
@EqualsAndHashCode(callSuper = true)
@Data
@TableName("missive")
public class MissiveEntity extends BaseEntity {

    /**
     * 流水号（自增id）
     */
    @TableId
    private Long id;

    /**
     * 公文名称
     */
    private String name;

    /**
     * 公文类型（1通告、2指示、3议案、4决议、5命令）
     */
    private Integer type;

    /**
     * 机密程度（1公开，2部门）
     */
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
    private Long authorId;

    /**
     * 拟稿部门id
     */
    private Long authorDeptId;

    /**
     * 正文
     */
    private String content;

    /**
     * 附件id
     */
    private Long attachmentId;

}
