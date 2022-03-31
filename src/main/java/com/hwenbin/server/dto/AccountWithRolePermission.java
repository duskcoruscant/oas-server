package com.hwenbin.server.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * @author hwb
 * @create 2022-03-14
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class AccountWithRolePermission extends AccountWithRole {

    /**
     * 用户的角色对应的权限code
     */
    private List<String> permissionCodeList;

}
