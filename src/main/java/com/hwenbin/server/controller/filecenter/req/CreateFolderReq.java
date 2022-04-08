package com.hwenbin.server.controller.filecenter.req;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @author hwb
 * @create 2022-04-04
 */
@Data
public class CreateFolderReq {

    @NotBlank(message = "文件夹名称不能为空")
    private String filename;

    /**
     * 员工id
     */
    @NotNull
    private Long empId;

    /**
     * 当前文件目录id
     */
    @NotNull
    private Long parentId;

    /**
     * 创建共享文件夹
     */
    private Integer isShared;

}
