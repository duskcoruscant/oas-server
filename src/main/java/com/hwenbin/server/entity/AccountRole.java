package com.hwenbin.server.entity;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @author hwb
 * @create 2022-03-14
 */
@Data
@Table(name = "account_role")
public class AccountRole {

    /**
     * 用户Id
     */
    @Id
    @Column(name = "account_id")
    private Long accountId;

    /**
     * 角色Id
     */
    @Column(name = "role_id")
    private Long roleId;

}
