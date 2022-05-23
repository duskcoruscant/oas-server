package com.hwenbin.server.controller.filecenter.req;

import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @author hwb
 * @date 2022/05/22 22:33
 */
@Data
public class ShareFileToReq {

    /**
     * 文件ID
     */
    @NotNull(message = "文件ID不能为空")
    private Long fileId;

    /**
     * 文件挂载目录
     */
    @NotNull(message = "文件挂载目录不能为空")
    private Long mountFolderId;

}
