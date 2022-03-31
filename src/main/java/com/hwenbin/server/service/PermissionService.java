package com.hwenbin.server.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hwenbin.server.dto.PermissionDTO;
import com.hwenbin.server.entity.Permission;

import java.util.List;

/**
 * @author hwb
 * @create 2022-03-14
 */
public interface PermissionService extends IService<Permission> {

    /**
     * 获取权限列表
     * @return 权限列表
     */
    List<PermissionDTO> getAllPermissionList();

}
