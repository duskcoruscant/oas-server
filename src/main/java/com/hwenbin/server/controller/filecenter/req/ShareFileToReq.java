package com.hwenbin.server.controller.filecenter.req;

import lombok.Data;

/**
 * @author hwb
 * @date 2022/05/22 22:33
 */
@Data
public class ShareFileToReq {

    /**
     * 文件ID
     */
    private Long fileId;

    /**
     * 文件挂载目录
     */
    private Long mountFolderId;

}
