package com.hwenbin.server.service.impl;

import com.hwenbin.server.core.flowable.factory.FlowServiceFactory;
import com.hwenbin.server.core.web.response.PageParam;
import com.hwenbin.server.core.web.response.PageResult;
import com.hwenbin.server.dto.flow.WorkflowDefinitionDTO;
import com.hwenbin.server.entity.WorkflowFormEntity;
import com.hwenbin.server.service.WorkflowDefinitionService;
import com.hwenbin.server.service.WorkflowDeployFormService;
import org.apache.commons.io.IOUtils;
import org.flowable.bpmn.model.BpmnModel;
import org.flowable.engine.repository.Deployment;
import org.flowable.engine.repository.DeploymentBuilder;
import org.flowable.engine.repository.ProcessDefinition;
import org.flowable.engine.repository.ProcessDefinitionQuery;
import org.flowable.image.impl.DefaultProcessDiagramGenerator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 流程定义Service业务层处理
 *
 * @author hwb
 * @date 2022/04/30 19:56
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class WorkflowDefinitionServiceImpl extends FlowServiceFactory implements WorkflowDefinitionService {

    @Resource
    private WorkflowDeployFormService deployFormService;

    private static final String BPMN_FILE_SUFFIX = ".bpmn";

    @Override
    public boolean exist(String processDefinitionKey) {
        ProcessDefinitionQuery processDefinitionQuery = repositoryService.createProcessDefinitionQuery()
                .processDefinitionKey(processDefinitionKey);
        long count = processDefinitionQuery.count();
        return count > 0;
    }

    @Override
    public PageResult<WorkflowDefinitionDTO> list(PageParam pageParam) {
        // 流程定义列表数据查询
        ProcessDefinitionQuery processDefinitionQuery = repositoryService.createProcessDefinitionQuery()
                .latestVersion()
                .orderByProcessDefinitionKey().asc();
        long pageTotal = processDefinitionQuery.count();
        if (pageTotal <= 0) {
            return PageResult.empty();
        }
        int offset = pageParam.getPageSize() * (pageParam.getPageNo() - 1);
        List<ProcessDefinition> definitionList = processDefinitionQuery.listPage(offset, pageParam.getPageSize());

        List<WorkflowDefinitionDTO> definitionDTOList = new ArrayList<>();
        for (ProcessDefinition processDefinition : definitionList) {
            String deploymentId = processDefinition.getDeploymentId();
            Deployment deployment = repositoryService.createDeploymentQuery().deploymentId(deploymentId).singleResult();
            WorkflowDefinitionDTO dto = new WorkflowDefinitionDTO();
            dto.setDefinitionId(processDefinition.getId());
            dto.setProcessKey(processDefinition.getKey());
            dto.setProcessName(processDefinition.getName());
            dto.setVersion(processDefinition.getVersion());
            dto.setCategory(processDefinition.getCategory());
            dto.setDeploymentId(processDefinition.getDeploymentId());
            dto.setSuspended(processDefinition.isSuspended());
            WorkflowFormEntity formEntity = deployFormService.selectDeployFormByDeployId(deploymentId);
            if (Objects.nonNull(formEntity)) {
                dto.setFormId(formEntity.getFormId());
                dto.setFormName(formEntity.getFormName());
            }
            // 流程定义时间
            dto.setCategory(deployment.getCategory());
            dto.setDeploymentTime(deployment.getDeploymentTime());
            definitionDTOList.add(dto);
        }
        return new PageResult<>(definitionDTOList, pageTotal);
    }

    @Override
    public PageResult<WorkflowDefinitionDTO> publishList(String processKey, PageParam pageParam) {
        // 创建查询条件
        ProcessDefinitionQuery processDefinitionQuery = repositoryService.createProcessDefinitionQuery()
                .processDefinitionKey(processKey)
                .orderByProcessDefinitionVersion().asc();
        long pageTotal = processDefinitionQuery.count();
        if (pageTotal <= 0) {
            return PageResult.empty();
        }
        // 根据查询条件，查询所有版本
        int offset = pageParam.getPageSize() * (pageParam.getPageNo() - 1);
        List<ProcessDefinition> processDefinitionList = processDefinitionQuery
                .listPage(offset, pageParam.getPageSize());
        List<WorkflowDefinitionDTO> definitionDTOList = processDefinitionList.stream().map(item -> {
            WorkflowDefinitionDTO dto = new WorkflowDefinitionDTO();
            dto.setDefinitionId(item.getId());
            dto.setProcessKey(item.getKey());
            dto.setProcessName(item.getName());
            dto.setVersion(item.getVersion());
            dto.setCategory(item.getCategory());
            dto.setDeploymentId(item.getDeploymentId());
            dto.setSuspended(item.isSuspended());
            // BeanUtil.copyProperties(item, dto);
            return dto;
        }).collect(Collectors.toList());
        return new PageResult<>(definitionDTOList, pageTotal);
    }

    @Override
    public void importFile(String name, String category, InputStream in) {
        String processName = name + BPMN_FILE_SUFFIX;
        // 创建流程部署
        DeploymentBuilder deploymentBuilder = repositoryService.createDeployment()
                .name(processName)
                .key(name)
                .category(category)
                .addInputStream(processName, in);
        // 部署
        deploymentBuilder.deploy();
    }

    @Override
    public String readXml(String definitionId) throws IOException {
        InputStream inputStream = repositoryService.getProcessModel(definitionId);
        return IOUtils.toString(inputStream, StandardCharsets.UTF_8.name());
    }

    @Override
    public void updateState(Boolean suspended, String definitionId) {
        if (!suspended) {
            // 激活
            repositoryService.activateProcessDefinitionById(definitionId, true, null);
        } else {
            // 挂起
            repositoryService.suspendProcessDefinitionById(definitionId, true, null);
        }
    }

    @Override
    public void delete(String deployId) {
        // true 允许级联删除 ,不设置会导致数据库外键关联异常
        repositoryService.deleteDeployment(deployId, true);
    }

    @Override
    public InputStream readImage(String definitionId) {
        //获得图片流
        DefaultProcessDiagramGenerator diagramGenerator = new DefaultProcessDiagramGenerator();
        BpmnModel bpmnModel = repositoryService.getBpmnModel(definitionId);
        //输出为图片
        return diagramGenerator.generateDiagram(
                bpmnModel,
                "png",
                Collections.emptyList(),
                Collections.emptyList(),
                "宋体",
                "宋体",
                "宋体",
                null,
                1.0,
                false);
    }

}
