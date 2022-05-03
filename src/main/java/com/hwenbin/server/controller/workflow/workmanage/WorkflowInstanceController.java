package com.hwenbin.server.controller.workflow.workmanage;

import com.hwenbin.server.core.web.response.CommonResult;
import com.hwenbin.server.core.web.response.ResultGenerator;
import com.hwenbin.server.entity.WorkflowTaskEntity;
import com.hwenbin.server.service.WorkflowInstanceService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * 工作流流程实例管理
 *
 * @author hwb
 * @date 2022/05/02 21:09
 */
@RestController
@RequestMapping("/workflow/instance")
public class WorkflowInstanceController {

    @Resource
    private WorkflowInstanceService instanceService;

    /**
     * 激活或挂起流程实例
     * @param state 1:激活,2:挂起
     * @param instanceId 流程实例ID
     * @return
     */
    @PostMapping(value = "/updateState")
    public CommonResult<Void> updateState(@RequestParam Integer state, @RequestParam String instanceId) {
        instanceService.updateState(state, instanceId);
        return ResultGenerator.genOkResult();
    }

    /**
     * 结束流程实例
     * @param bo
     * @return
     */
    @PostMapping(value = "/stopProcessInstance")
    public CommonResult<Void> stopProcessInstance(@RequestBody WorkflowTaskEntity bo) {
        instanceService.stopProcessInstance(bo);
        return ResultGenerator.genOkResult();
    }

    /**
     * 删除流程实例
     * @param instanceId 流程实例ID
     * @param deleteReason 删除原因
     * @return
     */
    @DeleteMapping(value = "/delete")
    public CommonResult<Void> delete(@RequestParam String instanceId,
                    @RequestParam(required = false) String deleteReason) {
        instanceService.delete(instanceId, deleteReason);
        return ResultGenerator.genOkResult();
    }

    /**
     * 查询流程实例详情信息
     * @param procInsId
     * @param deployId
     * @return
     */
    @GetMapping("/detail")
    public CommonResult<?> detail(String procInsId, String deployId) {
        return ResultGenerator.genOkResult(instanceService.queryDetailProcess(procInsId, deployId));
    }

}
