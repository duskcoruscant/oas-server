package com.hwenbin.server.service.impl;

import com.hwenbin.server.core.service.AbstractService;
import com.hwenbin.server.dto.RoleWithPermission;
import com.hwenbin.server.dto.RoleWithResource;
import com.hwenbin.server.entity.Role;
import com.hwenbin.server.entity.RolePermission;
import com.hwenbin.server.mapper.PermissionMapper;
import com.hwenbin.server.mapper.RoleMapper;
import com.hwenbin.server.mapper.RolePermissionMapper;
import com.hwenbin.server.service.RoleService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Condition;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author hwb
 * @create 2022-03-14
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class RoleServiceImpl extends AbstractService<Role> implements RoleService {

    @Resource
    private RoleMapper roleMapper;

    @Resource
    private PermissionMapper permissionMapper;

    @Resource
    private RolePermissionMapper rolePermissionMapper;

    @Override
    public List<RoleWithResource> listRoleWithPermission() {
        // 由于mybatis在嵌套查询时和pagehelper有冲突
        // 暂时用for循环代替
        final List<RoleWithResource> roles = this.roleMapper.listRoles();
        roles.forEach(
                role -> {
                    final List<com.hwenbin.server.entity.Resource> resources =
                            this.permissionMapper.listRoleWithResourceByRoleId(role.getId());
                    role.setResourceList(resources);
                });
        return roles;
    }

    @Override
    public void save(final RoleWithPermission role) {
        this.roleMapper.insert(role);
        this.rolePermissionMapper.saveRolePermission(role.getId(), role.getPermissionIdList());
    }

    @Override
    public void update(final RoleWithPermission role) {
        // 删掉所有权限，再添加回去
        final Condition condition = new Condition(RolePermission.class);
        condition.createCriteria().andCondition("role_id = ", role.getId());
        this.rolePermissionMapper.deleteByCondition(condition);
        this.rolePermissionMapper.saveRolePermission(role.getId(), role.getPermissionIdList());
        this.roleMapper.updateTimeById(role.getId());
    }

}
