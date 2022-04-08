package com.hwenbin.server.controller.filecenter.req;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @author hwb
 * @create 2022-04-06
 */
@Data
public class RenameFileReq {

    @NotNull(message = "文件id不能为空")
    private Long id;

    @NotBlank(message = "文件名称不能为空")
    private String filename;

}
