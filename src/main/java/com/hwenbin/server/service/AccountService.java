package com.hwenbin.server.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hwenbin.server.dto.AccountDTO;
import com.hwenbin.server.dto.AccountWithRolePermission;
import com.hwenbin.server.entity.Account;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

/**
 * @author hwb
 * @create 2022-03-14
 */
public interface AccountService extends IService<Account> {

    /**
     * 获取 token
     * @param name nickname
     * @return token
     */
    String getToken(String name);

    /**
     * 注册账户
     * @param accountDto 账户信息
     */
    void register(AccountDTO accountDto);

    String login(Account account);

    /**
     * 按条件查询用户信息
     *
     * @param column 列名
     * @param params 参数
     * @return 用户
     */
    AccountWithRolePermission findDetailBy(String column, Object params);

    /**
     * 按用户名查询用户信息
     *
     * @param name 用户名
     * @return 用户
     * @throws UsernameNotFoundException 用户名找不到
     */
    AccountWithRolePermission findDetailByName(String name) throws UsernameNotFoundException;

    /**
     * 按用户名更新最后一次登录时间
     *
     * @param nickname 用户名
     */
    void updateLoginTimeByName(String nickname);

    /**
     * 验证用户密码
     *
     * @param rawPassword     原密码
     * @param encodedPassword 加密后的密码
     * @return boolean
     */
    Boolean verifyPassword(String rawPassword, String encodedPassword);

    /**
     * 更新自己的资料
     * @param account 更新的账户信息
     * @return token
     */
    String updateMe(Account account);

}
