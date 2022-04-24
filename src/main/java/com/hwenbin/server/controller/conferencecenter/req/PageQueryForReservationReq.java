package com.hwenbin.server.controller.conferencecenter.req;

import com.hwenbin.server.core.constant.ProjectConstant;
import com.hwenbin.server.core.web.response.PageParam;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;

/**
 * @author hwb
 * @date 2022/04/20 23:28
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class PageQueryForReservationReq extends PageParam {

    /**
     * 会议设备列表
     */
    private List<String> equipmentNames;

    /**
     * 预订日期
     */
    @NotNull(message = "预订日期不能为空")
    @DateTimeFormat(pattern = ProjectConstant.FORMAT_YEAR_MONTH_DAY_WITH_FILE_SEPARATOR_INNER)
    private Date resDate;

    /**
     * 会议室编号
     */
    private String roomCode;

}
