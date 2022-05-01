package com.hwenbin.server.controller.workflow.flowmanage.req;

import com.hwenbin.server.core.web.response.PageParam;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author hwb
 * @date 2022/04/29 14:08
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class PageQueryForWorkflowCategoryReq extends PageParam {

    /**
     * 分类名称
     */
    private String categoryName;

    /**
     * 分类编码
     */
    private String code;

}
