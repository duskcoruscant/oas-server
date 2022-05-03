package com.hwenbin.server.controller.workflow.workmanage;

import com.hwenbin.server.core.web.response.CommonResult;
import com.hwenbin.server.core.web.response.ResultGenerator;
import com.hwenbin.server.entity.WorkflowFormEntity;
import com.hwenbin.server.service.WorkflowDeployFormService;
import com.hwenbin.server.util.JsonUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Map;
import java.util.Objects;

/**
 * 流程部署
 *
 * @author hwb
 * @date 2022/05/01 22:48
 */
@RestController
@RequestMapping("/workflow/deploy")
public class WorkflowDeployController {

    @Resource
    private WorkflowDeployFormService deployFormService;

    /**
     * 查询流程部署关联表单信息
     * @param deployId 流程部署id
     * @return map
     */
    @GetMapping("/form/{deployId}")
    public CommonResult<?> start(@PathVariable(value = "deployId") String deployId) {
        WorkflowFormEntity formEntity = deployFormService.selectDeployFormByDeployId(deployId);
        if (Objects.isNull(formEntity)) {
            return ResultGenerator.genFailedResult("请先配置流程表单");
        }
        return ResultGenerator.genOkResult(JsonUtils.parseObject(formEntity.getContent(), Map.class));
    }

}
