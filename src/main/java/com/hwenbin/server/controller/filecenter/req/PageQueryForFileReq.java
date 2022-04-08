package com.hwenbin.server.controller.filecenter.req;

import com.hwenbin.server.core.web.response.PageParam;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotNull;

/**
 * @author hwb
 * @create 2022-04-03
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class PageQueryForFileReq extends PageParam {

    /**
     * 父级id，即当前要查询的目录，为0即根目录
     */
    @NotNull(message = "当前目录不能为空")
    private Long parentId;

    /**
     * 文件名称 —— 搜索
     */
    private String name;

    /**
     * 查询共享文件
     */
    private Integer isShared;

    /**
     * 员工id —— 个人文件
     */
    private Long empId;

    /**
     * 回收站
     */
    private Integer isDeleted;

}
