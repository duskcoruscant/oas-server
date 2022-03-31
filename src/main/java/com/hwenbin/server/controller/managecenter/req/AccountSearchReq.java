package com.hwenbin.server.controller.managecenter.req;

import com.hwenbin.server.core.web.response.PageParam;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author hwb
 * @create 2022-03-26
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class AccountSearchReq extends PageParam {

    private String accountName;

    private String roleName;

}
