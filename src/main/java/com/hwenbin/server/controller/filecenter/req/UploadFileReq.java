package com.hwenbin.server.controller.filecenter.req;

import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @author hwb
 * @create 2022-04-05
 */
@Data
public class UploadFileReq {

    /**
     * 员工id
     */
    @NotNull(message = "员工id不能为空")
    private Long empId;

    /**
     * 上传操作所在目录
     */
    @NotNull(message = "上传目录不能为空")
    private Long parentId;

    /**
     * 上传共享文件
     */
    private Integer isShared;

}
