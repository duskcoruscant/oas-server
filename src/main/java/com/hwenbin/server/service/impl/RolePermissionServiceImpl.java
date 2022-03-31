package com.hwenbin.server.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.ImmutableMap;
import com.hwenbin.server.core.mybatis.query.MyLambdaQueryWrapper;
import com.hwenbin.server.entity.Permission;
import com.hwenbin.server.entity.RolePermission;
import com.hwenbin.server.mapper.RolePermissionMapper;
import com.hwenbin.server.service.PermissionService;
import com.hwenbin.server.service.RolePermissionService;
import com.hwenbin.server.service.RoleService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author hwb
 * @create 2022-03-14
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class RolePermissionServiceImpl extends ServiceImpl<RolePermissionMapper, RolePermission>
        implements RolePermissionService {

    @Resource
    private RolePermissionMapper rolePermissionMapper;

    @Resource
    private RoleService roleService;

    @Resource
    private PermissionService permissionService;

    @Override
    public void deleteItemByRoleId(Long roleId) {
        rolePermissionMapper.deleteByMap(ImmutableMap.of("role_id", roleId));
    }

    @Override
    public List<Long> getRoleRelatedPermissionIds(Long roleId) {
        if ("admin".equals(roleService.getRoleById(roleId).getCode())) {
            return permissionService.list().stream().map(Permission::getId).collect(Collectors.toList());
        }
        return rolePermissionMapper.selectList("role_id", roleId)
                .stream().map(RolePermission::getPermissionId)
                .collect(Collectors.toList());
    }

    @Override
    public void assignRolePermission(Long roleId, Set<Long> permissionIds) {
        roleService.checkRoleExistsAndBuiltin(roleId, true);
        // 获取角色已拥有的权限列表
        List<Long> dbPermissionIds = getRoleRelatedPermissionIds(roleId);
        // 分别计算需要新增和移除的权限列表
        Collection<Long> createPermissionIds = CollUtil.subtract(permissionIds, dbPermissionIds);
        Collection<Long> removePermissionIds = CollUtil.subtract(dbPermissionIds, permissionIds);
        if (CollUtil.isNotEmpty(createPermissionIds)) {
            rolePermissionMapper.insertBatch(
                    createPermissionIds.stream()
                            .map(permissionId -> new RolePermission(null, roleId, permissionId))
                            .collect(Collectors.toList())
            );
        }
        if (CollUtil.isNotEmpty(removePermissionIds)) {
            rolePermissionMapper.delete(
                    new MyLambdaQueryWrapper<RolePermission>()
                            .eq(RolePermission::getRoleId, roleId)
                            .in(RolePermission::getPermissionId, removePermissionIds)
            );
        }
    }

}
