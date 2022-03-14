package com.hwenbin.server.mapper;

import com.hwenbin.server.core.mapper.MyMapper;
import com.hwenbin.server.dto.AccountWithRole;
import com.hwenbin.server.dto.AccountWithRolePermission;
import com.hwenbin.server.entity.Account;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * @author hwb
 * @create 2022-03-14
 */
public interface AccountMapper extends MyMapper<Account> {

    /**
     * 获取所有用户以及对应角色
     *
     * @return 用户列表
     */
    List<AccountWithRole> listAllWithRole();

    /**
     * 按条件获取用户
     *
     * @param params 参数
     * @return 用户列表
     */
    List<AccountWithRole> findWithRoleBy(final Map<String, Object> params);

    /**
     * 按条件查询用户信息
     *
     * @param params 参数
     * @return 用户
     */
    AccountWithRolePermission findDetailBy(Map<String, Object> params);

    /**
     * 按用户名更新最后登陆时间
     *
     * @param name 用户名
     */
    void updateLoginTimeByName(@Param("name") String name);

}
