package com.hwenbin.server.controller.managecenter.req;

import com.hwenbin.server.core.web.response.PageParam;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author hwb
 * @create 2022-03-28
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class GetPositionListReq extends PageParam {

    /**
     * 职位编码
     */
    private String code;

    /**
     * 职位名称
     */
    private String name;

    /**
     * 职位状态
     */
    private Integer status;

}
