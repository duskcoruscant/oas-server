package com.hwenbin.server.service.impl;

import com.hwenbin.server.core.service.AbstractService;
import com.hwenbin.server.entity.Permission;
import com.hwenbin.server.mapper.PermissionMapper;
import com.hwenbin.server.service.PermissionService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author hwb
 * @create 2022-03-14
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class PermissionServiceImpl extends AbstractService<Permission>
        implements PermissionService {

    @Resource
    private PermissionMapper permissionMapper;

    @Override
    public List<com.hwenbin.server.entity.Resource> listResourceWithHandle() {
        return this.permissionMapper.listResourceWithHandle();
    }

    @Override
    public List<com.hwenbin.server.entity.Resource> listRoleWithResourceByRoleId(Long roleId) {
        return this.permissionMapper.listRoleWithResourceByRoleId(roleId);
    }

}
