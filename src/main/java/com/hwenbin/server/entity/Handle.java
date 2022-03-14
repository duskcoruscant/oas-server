package com.hwenbin.server.entity;

import lombok.Data;

import javax.persistence.Transient;

/**
 * @author hwb
 * @create 2022-03-14
 */
@Data
public class Handle {

    /**
     * 对应权限id
     */
    @Transient
    private Long id;

    /**
     * 操作名称
     */
    @Transient
    private String handle;

}
