package com.hwenbin.server.dto;

import com.hwenbin.server.entity.Account;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Transient;

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
    @Transient
    private Long roleId;

    /**
     * 用户的角色名
     */
    @Transient
    private String roleName;

}
