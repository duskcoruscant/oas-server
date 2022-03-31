package com.hwenbin.server.dto;

import com.hwenbin.server.entity.Account;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author hwb
 * @create 2022-03-14
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class AccountWithRole extends Account {

    /**
     * 用户的角色Id
     */
    private Long roleId;

    /**
     * 用户的角色名
     */
    private String roleName;

}
