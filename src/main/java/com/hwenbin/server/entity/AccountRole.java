package com.hwenbin.server.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author hwb
 * @create 2022-03-14
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("account_role")
public class AccountRole {

    /**
     * 自增编号
     */
    private Long id;

    /**
     * 用户Id
     */
    private Long accountId;

    /**
     * 角色Id
     */
    private Long roleId;

}
