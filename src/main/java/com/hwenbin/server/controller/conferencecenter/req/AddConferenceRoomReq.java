package com.hwenbin.server.controller.conferencecenter.req;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Set;

/**
 * @author hwb
 * @date 2022/04/14 14:08
 */
@Data
public class AddConferenceRoomReq {

    /**
     * 会议室编号
     */
    @NotEmpty(message = "会议室编号不能为空")
    private String code;

    /**
     * 会议室地址
     */
    @NotEmpty(message = "会议室地址不能为空")
    private String address;

    /**
     * 状态（0正常，1停用）
     */
    @NotNull(message = "会议室状态不能为空")
    private Integer status;

    /**
     * 会议室设备id集合
     */
    private Set<Long> conferenceEquipmentIds;

}
