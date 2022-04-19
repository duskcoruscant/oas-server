package com.hwenbin.server.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 公文类型（1通告、2指示、3议案、4决议、5命令）
 *
 * @author hwb
 * @date 2022/04/17 23:53
 */
@Getter
@AllArgsConstructor
public enum MissiveTypeEnum {

    /**
     * 通告
     */
    ANNOUNCE(1, "通告"),

    /**
     * 指示
     */
    DIRECTION(2, "指示"),

    /**
     * 议案
     */
    MOTION(3, "议案"),

    /**
     * 决议
     */
    RESOLUTION(4, "决议"),

    /**
     * 命令
     */
    ORDER(5, "命令")
    ;

    /**
     * 类型值
     */
    private final Integer value;

    /**
     * 类型名称
     */
    private final String name;

}
