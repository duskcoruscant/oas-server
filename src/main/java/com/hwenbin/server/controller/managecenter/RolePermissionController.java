package com.hwenbin.server.controller.managecenter;

import com.hwenbin.server.controller.managecenter.req.AssignRolePermissionReq;
import com.hwenbin.server.core.web.response.CommonResult;
import com.hwenbin.server.core.web.response.ResultGenerator;
import com.hwenbin.server.service.RolePermissionService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.List;

/**
 * @author hwb
 * @create 2022-03-30
 */
@RestController
@RequestMapping("/role/permission")
public class RolePermissionController {

    @Resource
    private RolePermissionService rolePermissionService;

    /**
     * 获取角色关联的所有权限id
     * @param id 角色id
     * @return 权限列表
     */
    @PreAuthorize("hasAuthority('manage:role:assign-role-menu')")
    @GetMapping("/getRoleRelatedPermissionIds")
    public CommonResult<List<Long>> getRoleRelatedPermissionIds(@RequestParam final Long id) {
        return ResultGenerator.genOkResult(
                rolePermissionService.getRoleRelatedPermissionIds(id)
        );
    }

    /**
     * 给角色分配指定的权限
     * @param req 参数
     * @return true
     */
    @PreAuthorize("hasAuthority('manage:role:assign-role-menu')")
    @PutMapping("/assign")
    public CommonResult<Boolean> assignRoleAppointedPermission(@Valid @RequestBody AssignRolePermissionReq req) {
        rolePermissionService.assignRolePermission(req.getRoleId(), req.getMenuIds());
        return ResultGenerator.genOkResult(true);
    }

}
