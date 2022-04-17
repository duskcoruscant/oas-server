package com.hwenbin.server.controller.conferencecenter.req;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

/**
 * @author hwb
 * @date 2022/04/13 17:36
 */
@Data
public class AddConferenceEquipmentReq {

    /**
     * 会议室设备编号
     */
    @NotEmpty(message = "会议室设备编号不能为空")
    private String code;

    /**
     * 会议室设备类型
     */
    @NotEmpty(message = "会议室设备类型不能为空")
    private String type;

    /**
     * 会议室设备使用状态（0空闲，1使用中，2损坏）
     */
    @NotNull(message = "会议室设备使用状态不能为空")
    private Integer status;

    /**
     * 设备关联会议室
     */
    private Long relatedRoomId;

}
