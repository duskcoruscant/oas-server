package com.hwenbin.server.mapper;

import com.hwenbin.server.core.mapper.MyMapper;
import com.hwenbin.server.entity.AccountRole;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

/**
 * @author hwb
 * @create 2022-03-14
 */
public interface AccountRoleMapper extends MyMapper<AccountRole> {

    /**
     * 更新用户角色
     *
     * @param accountRole 用户角色
     */
    // @Update(
    //         "UPDATE account_role SET role_id = #{accountRole.roleId} WHERE account_id = #{accountRole.accountId}")
    void updateRoleIdByAccountId(@Param("accountRole") AccountRole accountRole);

}
