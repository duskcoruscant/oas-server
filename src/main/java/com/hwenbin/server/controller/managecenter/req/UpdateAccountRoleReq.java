package com.hwenbin.server.controller.managecenter.req;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.Collections;
import java.util.Set;

/**
 * @author hwb
 * @create 2022-03-29
 */
@Data
public class UpdateAccountRoleReq {

    /**
     * 账户id
     */
    @NotNull(message = "账户id不能为空")
    private Long accountId;

    /**
     * 角色id列表
     */
    private Set<Long> roleIds = Collections.emptySet();

}
