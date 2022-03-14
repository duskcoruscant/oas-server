package com.hwenbin.server.dto;

import com.hwenbin.server.entity.Role;

import java.util.List;

/**
 * @author hwb
 * @create 2022-03-14
 */
public class RoleWithPermission extends Role {

    /**
     * 角色对应的权限Id列表
     */
    private List<Integer> permissionIdList;

    public List<Integer> getPermissionIdList() {
        return this.permissionIdList;
    }

    public void setPermissionIdList(final List<Integer> permissionIdList) {
        this.permissionIdList = permissionIdList;
    }

}
