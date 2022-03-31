package com.hwenbin.server.dto;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

/**
 * @author hwb
 * @create 2022-03-28
 */
@Data
public class PositionDTO {

    /**
     * 职位id
     */
    private Long id;

    /**
     * 职位编码
     */
    @NotEmpty(message = "职位编码不能为空")
    private String code;

    /**
     * 职位名称
     */
    @NotEmpty(message = "职位名称不能为空")
    private String name;

    /**
     * 显示顺序
     */
    @NotNull(message = "显示顺序不能为空")
    private Integer sort;

    /**
     * 状态
     */
    private Integer status;

    /**
     * 备注
     */
    private String remark;

}
