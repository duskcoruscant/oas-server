package com.hwenbin.server.mapper;

import com.hwenbin.server.core.mybatis.mapper.MyBaseMapper;
import com.hwenbin.server.dto.AccountWithRolePermission;
import com.hwenbin.server.entity.Account;
import org.apache.ibatis.annotations.Param;

import java.util.Map;

/**
 * @author hwb
 * @create 2022-03-14
 */
public interface AccountMapper extends MyBaseMapper<Account> {

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
     * @param nickname 用户名
     */
    void updateLoginTimeByName(@Param("nickname") String nickname);

}
