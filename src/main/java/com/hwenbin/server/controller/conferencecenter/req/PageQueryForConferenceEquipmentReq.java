package com.hwenbin.server.controller.conferencecenter.req;

import com.hwenbin.server.core.web.response.PageParam;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author hwb
 * @date 2022/04/13 16:59
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class PageQueryForConferenceEquipmentReq extends PageParam {

    /**
     * 设备编号
     */
    private String code;

    /**
     * 设备类型
     */
    private String type;

    /**
     * 设备状态
     */
    private Integer status;

}
