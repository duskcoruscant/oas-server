package com.hwenbin.server.mapper;

import com.hwenbin.server.core.mapper.MyMapper;
import com.hwenbin.server.entity.RolePermission;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author hwb
 * @create 2022-03-14
 */
public interface RolePermissionMapper extends MyMapper<RolePermission> {

    /**
     * 保存角色以及对应的权限ID
     *
     * @param roleId           角色ID
     * @param permissionIdList 权限ID列表
     */
    void saveRolePermission(
            @Param("roleId") Long roleId, @Param("permissionIdList") List<Integer> permissionIdList);

}
