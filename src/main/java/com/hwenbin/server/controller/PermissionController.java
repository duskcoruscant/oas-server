package com.hwenbin.server.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.hwenbin.server.core.response.Result;
import com.hwenbin.server.core.response.ResultGenerator;
import com.hwenbin.server.service.PermissionService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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

    @PreAuthorize("hasAuthority('role:list')")
    @GetMapping
    public Result listResourcePermission(
            @RequestParam(defaultValue = "0") final Integer page,
            @RequestParam(defaultValue = "0") final Integer size) {
        PageHelper.startPage(page, size);
        final List<com.hwenbin.server.entity.Resource> list =
                this.permissionService.listResourceWithHandle();
        final PageInfo<com.hwenbin.server.entity.Resource> pageInfo = new PageInfo<>(list);
        return ResultGenerator.genOkResult(pageInfo);
    }

}
