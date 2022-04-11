package com.hwenbin.server.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author hwb
 * @date 2022/04/10 21:12
 */
@Getter
@AllArgsConstructor
public enum WorkLogTypeEnums {

    /**
     * 日报
     */
    DAY_LOG(1, "日报"),

    /**
     * 周报
     */
    WEEK_LOG(2, "周报"),

    /**
     * 月报
     */
    MONTH_LOG(3, "月报")
    ;

    /**
     * 类型值
     */
    private final Integer type;

    /**
     * 类型名称
     */
    private final String name;

}
