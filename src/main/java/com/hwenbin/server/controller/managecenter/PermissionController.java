package com.hwenbin.server.controller.managecenter;

import com.hwenbin.server.core.web.response.CommonResult;
import com.hwenbin.server.core.web.response.ResultGenerator;
import com.hwenbin.server.dto.PermissionDTO;
import com.hwenbin.server.service.PermissionService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author hwb
 * @create 2022-03-14
 */
@RestController
@RequestMapping("/permission")
public class PermissionController {

    @Resource
    private PermissionService permissionService;

    /**
     * 获取权限列表
     * @return 权限列表
     */
    @PreAuthorize("hasAuthority('manage:role:assign-role-menu')")
    @GetMapping("/list")
    public CommonResult<List<PermissionDTO>> getAllPermissionList() {
        return ResultGenerator.genOkResult(permissionService.getAllPermissionList());
    }

}
