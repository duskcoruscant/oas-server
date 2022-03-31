package com.hwenbin.server.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hwenbin.server.entity.AccountRole;

import java.util.List;
import java.util.Set;

/**
 * @author hwb
 * @create 2022-03-14
 */
public interface AccountRoleService extends IService<AccountRole> {

    /**
     * 获取账户拥有的所有角色
     * @param accountId 账户id
     * @return 角色id列表
     */
    List<Long> getRoleIdsByAccountId(Long accountId);

    /**
     * 分配账户拥有的角色
     * @param accountId 账户id
     * @param roleIds 角色id列表
     */
    void assignAccountRole(Long accountId, Set<Long> roleIds);

    /**
     * 处理 角色删除操作中，相关数据的删除
     * @param roleId 角色id
     */
    void deleteItemByRoleId(Long roleId);

}
