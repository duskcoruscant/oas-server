package com.hwenbin.server.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author hwb
 * @date 2022/04/07 21:04
 */
@Getter
@AllArgsConstructor
public enum DeleteStatusEnum {

    /**
     * 未删除
     */
    NONE_DELETED(false, "未删除"),

    /**
     * 已删除
     */
    DELETED(true, "已删除")
    ;

    /**
     * 值
     */
    private final Boolean value;

    /**
     * 名称
     */
    private final String name;

}
