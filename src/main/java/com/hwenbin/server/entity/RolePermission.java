package com.hwenbin.server.entity;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @author hwb
 * @create 2022-03-14
 */
@Data
@Table(name = "role_permission")
public class RolePermission {

    /** 角色Id */
    @Id
    @Column(name = "role_id")
    private Long roleId;

    /** 权限Id */
    @Column(name = "permission_id")
    private Long permissionId;
    
}
