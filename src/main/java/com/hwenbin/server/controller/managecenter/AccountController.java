package com.hwenbin.server.controller.managecenter;

import com.hwenbin.server.core.jwt.JwtUtil;
import com.hwenbin.server.core.web.response.CommonResult;
import com.hwenbin.server.core.web.response.ResultGenerator;
import com.hwenbin.server.dto.AccountWithRolePermission;
import com.hwenbin.server.entity.Account;
import com.hwenbin.server.service.AccountService;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.security.Principal;

/**
 * @author hwb
 * @create 2022-03-14
 */
@RestController
@RequestMapping("/account")
@Validated
public class AccountController {

    @Resource
    private AccountService accountService;

    @Resource
    private JwtUtil jwtUtil;

    /**
     * 验证密码
     * @param account 验证信息
     * @return 是否验证成功
     */
    @PostMapping("/password")
    public CommonResult<Boolean> validatePassword(@RequestBody final Account account) {
        final Account dbAccount = this.accountService.getById(account.getId());
        final Boolean isValidate =
                this.accountService.verifyPassword(account.getPassword(), dbAccount.getPassword());
        return ResultGenerator.genOkResult(isValidate);
    }

    /**
     * 更新自己的资料
     * @param account 更新的账户信息
     * @return token
     */
    @PutMapping("/detail")
    public CommonResult<String> updateMe(@RequestBody final Account account) {
        return ResultGenerator.genOkResult(accountService.updateMe(account));
    }

    /**
     * 自己的资料
     * @param principal 登录信息
     * @return AccountWithRolePermission
     */
    @GetMapping("/detail")
    public CommonResult<AccountWithRolePermission> detail(final Principal principal) {
        AccountWithRolePermission detailWithRolePermission = this.accountService.findDetailByName(principal.getName());
        return ResultGenerator.genOkResult(detailWithRolePermission);
    }

    /**
     * 登录
     * @param account 登录信息
     * @return token
     */
    @PostMapping("/token")
    public CommonResult<String> login(@RequestBody final Account account) {
        return ResultGenerator.genOkResult(accountService.login(account));
    }

    /**
     * 注销
     * @param principal 登录信息
     * @return true
     */
    @DeleteMapping("/token")
    public CommonResult<Boolean> logout(final Principal principal) {
        this.jwtUtil.invalidRedisToken(principal.getName());
        return ResultGenerator.genOkResult();
    }

}
