package com.hwenbin.server.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author hwb
 * @date 2022/04/06 15:39
 */
@Getter
@AllArgsConstructor
public enum FileShareTypeEnum {

    /**
     * 非共享文件
     */
    NONE_SHARED(0, "非共享文件"),

    /**
     * 共享文件
     */
    IS_SHARED(1, "共享文件")
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
