package com.hwenbin.server.controller.managecenter;

import com.hwenbin.server.controller.managecenter.req.GetRolePageReq;
import com.hwenbin.server.controller.managecenter.req.UpdateRoleStatusReq;
import com.hwenbin.server.core.web.response.CommonResult;
import com.hwenbin.server.core.web.response.PageResult;
import com.hwenbin.server.core.web.response.ResultGenerator;
import com.hwenbin.server.dto.RoleDTO;
import com.hwenbin.server.entity.Role;
import com.hwenbin.server.service.RoleService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.List;

/**
 * @author hwb
 * @create 2022-03-14
 */
@RestController
@RequestMapping("/role")
public class RoleController {

    @Resource
    private RoleService roleService;

    /**
     * 获取角色列表，分页+搜索
     * @param req 分页参数 + 筛选条件
     * @return 分页结果
     */
    @PreAuthorize("hasAuthority('manage:role:query')")
    @GetMapping
    public CommonResult<PageResult<Role>> pageQuery(@Valid final GetRolePageReq req) {
        return ResultGenerator.genOkResult(roleService.pageQuery(req));
    }

    /**
     * 获取所有状态为 enable 的角色，用于下拉列表
     * @return enable 角色列表
     */
    @GetMapping("/listEnableRole")
    public CommonResult<List<Role>> listEnableRoleForSelect() {
        return ResultGenerator.genOkResult(roleService.getEnableRoleList());
    }

    /**
     * 新增角色
     * @param roleDTO 新增的角色信息
     * @return true
     */
    @PreAuthorize("hasAuthority('manage:role:add')")
    @GetMapping("/addRole")
    public CommonResult<Boolean> addRole(@Valid final RoleDTO roleDTO) {
        roleService.addRole(roleDTO);
        return ResultGenerator.genOkResult(true);
    }

    /**
     * 更新角色信息
     * @param roleDTO 更新的角色信息
     * @return true
     */
    @PreAuthorize("hasAuthority('manage:role:update')")
    @PutMapping("/updateRole")
    public CommonResult<Boolean> updateRole(@Valid @RequestBody final RoleDTO roleDTO) {
        roleService.updateRole(roleDTO);
        return ResultGenerator.genOkResult(true);
    }

    /**
     * 获取角色详细信息
     * @param id 角色id
     * @return 角色信息
     */
    @PreAuthorize("hasAuthority('manage:role:query')")
    @GetMapping("/getRoleById")
    public CommonResult<Role> getRoleById(@RequestParam final Long id) {
        return ResultGenerator.genOkResult(roleService.getRoleById(id));
    }

    /**
     * 删除角色
     * @param id 角色id
     * @return true
     */
    @PreAuthorize("hasAuthority('manage:role:delete')")
    @DeleteMapping("/deleteRoleById")
    public CommonResult<Boolean> deleteRoleById(@RequestParam final Long id) {
        roleService.deleteRoleAndCascadeDataById(id);
        return ResultGenerator.genOkResult(true);
    }

    /**
     * 更新角色状态
     * @param req 参数：角色id + 状态
     * @return true
     */
    @PreAuthorize("hasAuthority('manage:role:update')")
    @PutMapping("/updateStatus")
    public CommonResult<Boolean> updateRoleStatus(@Valid @RequestBody UpdateRoleStatusReq req) {
        roleService.updateRoleStatus(req.getId(), req.getStatus());
        return ResultGenerator.genOkResult(true);
    }

}
