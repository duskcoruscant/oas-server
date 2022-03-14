package com.hwenbin.server.service;

import com.hwenbin.server.core.service.Service;
import com.hwenbin.server.entity.AccountRole;

/**
 * @author hwb
 * @create 2022-03-14
 */
public interface AccountRoleService extends Service<AccountRole> {

    /**
     * 更新用户角色
     *
     * @param accountRole 用户角色
     */
    void updateRoleIdByAccountId(AccountRole accountRole);

}
