package com.hwenbin.server.controller.workflow.flowmanage.req;

import com.hwenbin.server.core.web.response.PageParam;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author hwb
 * @date 2022/04/29 18:58
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class PageQueryForWorkflowFormReq extends PageParam {

    /**
     * 表单名称
     */
    private String formName;

}
