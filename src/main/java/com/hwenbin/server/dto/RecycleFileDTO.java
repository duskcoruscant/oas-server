package com.hwenbin.server.dto;

import lombok.Data;

import java.util.Date;

/**
 * @author hwb
 * @date 2022/04/08 20:21
 */
@Data
public class RecycleFileDTO {

    /**
     * 回收站文件id
     */
    private Long id;

    /**
     * 删除时间
     */
    private Date deletedTime;

    /**
     * 文件名称
     */
    private String name;

    /**
     * 文件类型
     */
    private String type;

    /**
     * 文件大小
     */
    private String size;

}
