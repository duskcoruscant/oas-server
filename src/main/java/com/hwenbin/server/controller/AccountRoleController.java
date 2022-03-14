package com.hwenbin.server.controller;

import com.hwenbin.server.core.response.Result;
import com.hwenbin.server.core.response.ResultGenerator;
import com.hwenbin.server.entity.AccountRole;
import com.hwenbin.server.service.AccountRoleService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.security.Principal;

/**
 * @author hwb
 * @create 2022-03-14
 */
@RestController
@RequestMapping("/account/role")
public class AccountRoleController {

    @Resource
    private AccountRoleService accountRoleService;

    @PreAuthorize("hasAuthority('role:update')")
    @PutMapping
    public Result updateAccountRole(
            @RequestBody final AccountRole accountRole, final Principal principal) {
        final AccountRole dbAccountRole =
                this.accountRoleService.getBy("accountId", accountRole.getAccountId());
        this.accountRoleService.updateRoleIdByAccountId(accountRole);
        return ResultGenerator.genOkResult();
    }

}
