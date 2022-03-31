package com.hwenbin.server.controller.managecenter;

import com.hwenbin.server.controller.managecenter.req.UpdateAccountRoleReq;
import com.hwenbin.server.core.web.response.CommonResult;
import com.hwenbin.server.core.web.response.ResultGenerator;
import com.hwenbin.server.service.AccountRoleService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.List;

/**
 * @author hwb
 * @create 2022-03-14
 */
@RestController
@RequestMapping("/account/role")
public class AccountRoleController {

    @Resource
    private AccountRoleService accountRoleService;

    /**
     * 获取 账户 拥有的所有 角色
     * @param id 账户id
     * @return 角色id列表
     */
    @PreAuthorize("hasAuthority('manage:employee:assign-role')")
    @GetMapping
    public CommonResult<List<Long>> getRoleIdsByAccountId(@RequestParam final Long id) {
        return ResultGenerator.genOkResult(accountRoleService.getRoleIdsByAccountId(id));
    }

    /**
     * 分配账户角色
     * @param req 参数
     * @return true
     */
    @PreAuthorize("hasAuthority('manage:employee:assign-role')")
    @PutMapping
    public CommonResult<Boolean> assignAccountRole(@Valid @RequestBody final UpdateAccountRoleReq req) {
        accountRoleService.assignAccountRole(req.getAccountId(), req.getRoleIds());
        return ResultGenerator.genOkResult(true);
    }

}
