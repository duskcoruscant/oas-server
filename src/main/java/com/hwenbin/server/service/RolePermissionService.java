package com.hwenbin.server.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hwenbin.server.entity.RolePermission;

import java.util.List;
import java.util.Set;

/**
 * @author hwb
 * @create 2022-03-14
 */
public interface RolePermissionService extends IService<RolePermission> {

    /**
     * 处理 角色删除操作中，相关数据的删除
     * @param roleId 角色id
     */
    void deleteItemByRoleId(Long roleId);

    /**
     * 获取角色关联的所有权限id
     * @param roleId 角色id
     * @return 权限id列表
     */
    List<Long> getRoleRelatedPermissionIds(Long roleId);

    /**
     * 给角色分配指定的权限
     * @param roleId 角色id
     * @param permissionIds 权限id列表
     */
    void assignRolePermission(Long roleId, Set<Long> permissionIds);

}
