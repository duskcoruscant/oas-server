package com.hwenbin.server.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.ImmutableMap;
import com.hwenbin.server.core.mybatis.query.MyLambdaQueryWrapper;
import com.hwenbin.server.entity.AccountRole;
import com.hwenbin.server.mapper.AccountRoleMapper;
import com.hwenbin.server.service.AccountRoleService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author hwb
 * @create 2022-03-14
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class AccountRoleServiceImpl extends ServiceImpl<AccountRoleMapper, AccountRole> implements AccountRoleService {

    @Resource
    private AccountRoleMapper accountRoleMapper;

    @Override
    public List<Long> getRoleIdsByAccountId(Long accountId) {
        return accountRoleMapper.selectList("account_id", accountId)
                .stream().map(AccountRole::getRoleId)
                .collect(Collectors.toList());
    }

    @Override
    public void assignAccountRole(Long accountId, Set<Long> roleIds) {
        // 账户已有角色id列表
        List<Long> dbRoleIds = this.getRoleIdsByAccountId(accountId);
        // 新增的角色id
        Collection<Long> createRoleIds = CollUtil.subtract(roleIds, dbRoleIds);
        // 移除的角色id
        Collection<Long> removeRoleIds = CollUtil.subtract(dbRoleIds, roleIds);
        // 对新增和移除的角色进行处理
        if (CollUtil.isNotEmpty(createRoleIds)) {
            accountRoleMapper.insertBatch(
                    createRoleIds.stream()
                            .map(roleId -> new AccountRole(null, accountId, roleId))
                            .collect(Collectors.toList())
            );
        }
        if (CollUtil.isNotEmpty(removeRoleIds)) {
            accountRoleMapper.delete(
                    new MyLambdaQueryWrapper<AccountRole>()
                            .eq(AccountRole::getAccountId, accountId)
                            .in(AccountRole::getRoleId, removeRoleIds)
            );
        }
    }

    @Override
    public void deleteItemByRoleId(Long roleId) {
        accountRoleMapper.deleteByMap(ImmutableMap.of("role_id", roleId));
    }

}
