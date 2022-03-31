package com.hwenbin.server.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author hwb
 * @create 2022-03-14
 */
@TableName("role_permission")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RolePermission {

    /**
     * 自增编号
     */
    @TableId
    private Long id;

    /** 角色Id */
    private Long roleId;

    /** 权限Id */
    private Long permissionId;
    
}
