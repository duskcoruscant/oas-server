package com.hwenbin.server.controller.managecenter.req;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.Collections;
import java.util.Set;

/**
 * @author hwb
 * @create 2022-03-31
 */
@Data
public class AssignRolePermissionReq {

    /**
     * 角色id
     */
    @NotNull(message = "角色id不能为空")
    private Long roleId;

    /**
     * 菜单权限id列表
     */
    private Set<Long> menuIds = Collections.emptySet();

}
