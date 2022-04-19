package com.hwenbin.server.controller.missivecenter.req;

import com.hwenbin.server.core.constant.ProjectConstant;
import com.hwenbin.server.core.web.response.PageParam;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * @author hwb
 * @date 2022/04/18 14:09
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class PageQueryForMissiveReq extends PageParam {

    /**
     * 员工id
     */
    @NotNull(message = "员工id不能为空")
    private Long empId;

    /**
     * 流水号
     */
    private Long id;

    /**
     * 公文名称
     */
    private String name;

    /**
     * 公文类型
     */
    private Integer type;

    /**
     * 拟稿人
     */
    private String authorId;

    /**
     * 签发日期 —— 开始
     */
    @DateTimeFormat(pattern = ProjectConstant.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private Date beginTime;

    /**
     * 签发日期 —— 结束
     */
    @DateTimeFormat(pattern = ProjectConstant.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private Date endTime;

}
