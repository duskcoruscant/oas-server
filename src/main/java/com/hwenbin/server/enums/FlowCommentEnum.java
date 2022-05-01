package com.hwenbin.server.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 流程意见类型
 *
 * @author hwb
 * @date 2022/04/30 00:45
 */
@Getter
@AllArgsConstructor
public enum FlowCommentEnum {

    /**
     * 说明
     */
    NORMAL("1", "正常"),

    REBACK("2", "退回"),

    REJECT("3", "驳回"),

    DELEGATE("4", "委派"),

    TRANSFER("5", "转办"),

    STOP("6", "终止");

    /**
     * 类型
     */
    private final String type;

    /**
     * 说明
     */
    private final String remark;

}
