package com.hwenbin.server.controller.conferencecenter.req;

import com.hwenbin.server.core.web.response.PageParam;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Set;

/**
 * @author hwb
 * @date 2022/04/14 14:29
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class PageQueryForConferenceRoomReq extends PageParam {

    /**
     * 会议室编号
     */
    private String code;

    /**
     * 会议室地址
     */
    private String address;

    /**
     * 会议室状态
     */
    private Integer status;

    /**
     * 会议室设备类型集合
     */
    private Set<String> conferenceEquipmentTypes;

}
