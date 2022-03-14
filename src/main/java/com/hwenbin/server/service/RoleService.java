package com.hwenbin.server.service;

import com.hwenbin.server.core.service.Service;
import com.hwenbin.server.dto.RoleWithPermission;
import com.hwenbin.server.dto.RoleWithResource;
import com.hwenbin.server.entity.Role;

import java.util.List;

/**
 * @author hwb
 * @create 2022-03-14
 */
public interface RoleService extends Service<Role> {

    /**
     * 新建角色
     *
     * @param roleWithPermission 带权限列表的角色
     */
    void save(RoleWithPermission roleWithPermission);

    /**
     * 更新角色
     *
     * @param roleWithPermission 带权限列表的角色
     */
    void update(RoleWithPermission roleWithPermission);

    /**
     * 获取所有角色以及对应的权限
     *
     * @return 角色可控资源列表
     */
    List<RoleWithResource> listRoleWithPermission();

}
