package com.hwenbin.server.dto;

import lombok.Data;

/**
 * @author hwb
 * @create 2022-03-30
 */
@Data
public class PermissionDTO {

    /**
     * 权限id
     */
    private Long id;

    /**
     * 权限名称
     */
    private String name;

    /**
     * 父权限id
     */
    private Long parentId;

}
