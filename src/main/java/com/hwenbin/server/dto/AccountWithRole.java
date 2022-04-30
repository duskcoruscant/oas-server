package com.hwenbin.server.dto;

import com.hwenbin.server.entity.Account;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Collection;

/**
 * @author hwb
 * @create 2022-03-14
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class AccountWithRole extends Account {

    // /**
    //  * 用户的角色Id
    //  */
    // private Long roleId;
    //
    // /**
    //  * 用户的角色名
    //  */
    // private String roleName;

    /**
     * 用户拥有的角色
     */
    private Collection<SimpleRole> roles;

    @Data
    public static class SimpleRole {
        private Long roleId;
        private String roleName;
        private String roleCode;
        private Integer roleStatus;
    }

}
