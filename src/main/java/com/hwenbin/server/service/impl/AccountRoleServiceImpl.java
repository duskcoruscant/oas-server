package com.hwenbin.server.service.impl;

import com.hwenbin.server.core.service.AbstractService;
import com.hwenbin.server.entity.AccountRole;
import com.hwenbin.server.mapper.AccountRoleMapper;
import com.hwenbin.server.service.AccountRoleService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
 * @author hwb
 * @create 2022-03-14
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class AccountRoleServiceImpl extends AbstractService<AccountRole>
        implements AccountRoleService {

    @Resource
    private AccountRoleMapper accountRoleMapper;

    @Override
    public void updateRoleIdByAccountId(final AccountRole accountRole) {
        this.accountRoleMapper.updateRoleIdByAccountId(accountRole);
    }

}
