package com.hwenbin.server.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author hwb
 * @date 2022/04/14 16:14
 */
@Getter
@AllArgsConstructor
public enum ConferenceEquipmentStatusEnum {

    /**
     * 空闲
     */
    UNUSED(0, "空闲"),

    /**
     * 使用中
     */
    USING(1, "使用中"),

    /**
     * 损坏
     */
    BREAKDOWN(2, "损坏")
    ;

    /**
     * 会议室设备使用状态
     */
    private Integer status;

    /**
     * 会议室设备状态名称
     */
    private String name;

}
