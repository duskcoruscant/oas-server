package com.hwenbin.server.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 会议进行状态枚举
 *
 * @author hwb
 * @date 2022/04/23 17:56
 */
@Getter
@AllArgsConstructor
public enum ConferenceProcessStatusEnum {

    /**
     * 未开始
     */
    NONE_START(1, "未开始"),

    /**
     * 进行中
     */
    PROCESSING(2, "进行中"),

    /**
     * 已结束
     */
    COMPLETED(3, "已结束")
    ;

    /**
     * 状态值
     */
    private final Integer status;

    /**
     * 名称
     */
    private final String name;

}
