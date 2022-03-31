package com.hwenbin.server.controller.managecenter.req;

import com.hwenbin.server.core.constant.ProjectConstant;
import com.hwenbin.server.core.web.response.PageParam;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * @author hwb
 * @create 2022-03-30
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class GetRolePageReq extends PageParam {

    /**
     * 角色名称
     */
    private String name;

    /**
     * 角色标识
     */
    private String code;

    /**
     * 角色状态
     */
    private Integer status;

    /**
     * 创建时间 范围 —— 开始
     */
    @DateTimeFormat(pattern = ProjectConstant.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private Date beginTime;

    /**
     * 创建时间 范围 —— 结束
     */
    @DateTimeFormat(pattern = ProjectConstant.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private Date endTime;

}
