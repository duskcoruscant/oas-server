package com.hwenbin.server.service;

import com.hwenbin.server.core.service.Service;
import com.hwenbin.server.entity.Permission;
import com.hwenbin.server.entity.Resource;

import java.util.List;

/**
 * @author hwb
 * @create 2022-03-14
 */
public interface PermissionService extends Service<Permission> {

    /**
     * 找到所有权限可控资源
     *
     * @return 资源列表
     */
    List<Resource> listResourceWithHandle();

    /**
     * 找到角色权限可控资源
     *
     * @param roleId 角色id
     * @return 资源列表
     */
    List<Resource> listRoleWithResourceByRoleId(Long roleId);

}
