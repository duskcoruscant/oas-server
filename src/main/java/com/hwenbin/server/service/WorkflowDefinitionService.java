package com.hwenbin.server.service;

import com.hwenbin.server.core.web.response.PageParam;
import com.hwenbin.server.core.web.response.PageResult;
import com.hwenbin.server.dto.flow.WorkflowDefinitionDTO;

import java.io.IOException;
import java.io.InputStream;

/**
 * @author hwb
 * @date 2022/04/30 19:56
 */
public interface WorkflowDefinitionService {

    boolean exist(String processDefinitionKey);


    /**
     * 流程定义列表
     * @param pageParam 分页参数
     * @return 流程定义分页列表数据
     */
    PageResult<WorkflowDefinitionDTO> list(PageParam pageParam);

    /**
     *
     * @param processKey
     * @return
     */
    PageResult<WorkflowDefinitionDTO> publishList(String processKey, PageParam pageParam);

    /**
     * 导入流程文件
     *
     * @param name
     * @param category
     * @param in
     */
    void importFile(String name, String category, InputStream in);

    /**
     * 读取xml
     * @param definitionId 流程定义ID
     * @return
     */
    String readXml(String definitionId) throws IOException;


    /**
     * 激活或挂起流程定义
     *
     * @param suspended    状态
     * @param definitionId 流程定义ID
     */
    void updateState(Boolean suspended, String definitionId);


    /**
     * 删除流程定义
     *
     * @param deployId 流程部署ID act_ge_bytearray 表中 deployment_id值
     */
    void delete(String deployId);


    /**
     * 读取图片文件
     * @param definitionId 流程定义ID
     * @return
     */
    InputStream readImage(String definitionId);

}
