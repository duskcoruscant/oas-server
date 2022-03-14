package com.hwenbin.server.service.impl;

import com.hwenbin.server.core.service.AbstractService;
import com.hwenbin.server.entity.RolePermission;
import com.hwenbin.server.mapper.RolePermissionMapper;
import com.hwenbin.server.service.RolePermissionService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
 * @author hwb
 * @create 2022-03-14
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class RolePermissionServiceImpl extends AbstractService<RolePermission>
        implements RolePermissionService {

    @Resource
    private RolePermissionMapper rolePermissionMapper;

}
