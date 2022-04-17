package com.hwenbin.server.controller.conferencecenter.req;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

/**
 * @author hwb
 * @date 2022/04/14 16:02
 */
@Data
public class UpdateConferenceEquipmentReq {

    /**
     * 自增id
     */
    @NotNull(message = "会议室设备id不能为空")
    private Long id;

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
     * 关联会议室id
     */
    private Long relatedRoomId;

}
