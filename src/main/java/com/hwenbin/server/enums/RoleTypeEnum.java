package com.hwenbin.server.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 角色类型枚举
 *
 * @author hwb
 * @create 2022-03-31
 */
@Getter
@AllArgsConstructor
public enum RoleTypeEnum {

    BUILTIN(0, "内置"),
    CUSTOM(1, "自定义")
    ;

    /**
     * 类型值
     */
    private final Integer type;

    /**
     * 类型名
     */
    private final String name;

}
