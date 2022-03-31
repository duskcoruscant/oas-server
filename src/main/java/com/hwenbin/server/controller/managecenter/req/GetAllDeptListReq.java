package com.hwenbin.server.controller.managecenter.req;

import lombok.Data;

/**
 * @author hwb
 * @create 2022-03-29
 */
@Data
public class GetAllDeptListReq {

    /**
     * 部门名称
     */
    private String name;

    /**
     * 部门状态
     */
    private Integer status;

}
