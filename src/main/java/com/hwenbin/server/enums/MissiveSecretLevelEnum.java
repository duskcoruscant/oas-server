package com.hwenbin.server.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 公文机密程度（1公开，2部门）
 *
 * @author hwb
 * @date 2022/04/17 23:54
 */
@Getter
@AllArgsConstructor
public enum MissiveSecretLevelEnum {

    /**
     * 公开
     */
    PUBLIC(1, "公开"),

    /**
     * 部门
     */
    DEPARTMENT(2, "部门")
    ;

    /**
     * 级别
     */
    private final Integer level;

    /**
     * 名称
     */
    private final String name;

}
