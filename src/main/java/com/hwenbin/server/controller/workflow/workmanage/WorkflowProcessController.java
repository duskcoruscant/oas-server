package com.hwenbin.server.controller.workflow.workmanage;

import com.hwenbin.server.core.web.response.CommonResult;
import com.hwenbin.server.core.web.response.PageParam;
import com.hwenbin.server.core.web.response.PageResult;
import com.hwenbin.server.core.web.response.ResultGenerator;
import com.hwenbin.server.dto.flow.WorkflowDefinitionDTO;
import com.hwenbin.server.dto.flow.WorkflowTaskDTO;
import com.hwenbin.server.service.WorkflowProcessService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Map;

/**
 * 工作流流程管理
 *
 * @author hwb
 * @date 2022/05/01 15:42
 */
@RestController
@RequestMapping("/workflow/process")
public class WorkflowProcessController {

    @Resource
    private WorkflowProcessService processService;

    /**
     * 分页查询可发起流程列表
     * @param pageParam 分页参数
     * @return 结果
     */
    @GetMapping(value = "/list")
    public CommonResult<PageResult<WorkflowDefinitionDTO>> list(PageParam pageParam) {
        return ResultGenerator.genOkResult(processService.processList(pageParam));
    }

    /**
     * 根据流程定义id启动流程实例
     * @param processDefId 流程定义id
     * @param variables 变量集合,json对象
     * @return string
     */
    @PostMapping("/start/{processDefId}")
    public CommonResult<String> start(@PathVariable(value = "processDefId") String processDefId,
                         @RequestBody Map<String, Object> variables) {
        processService.startProcess(processDefId, variables);
        return ResultGenerator.genOkResult("流程启动成功");

    }

    /**
     * 我拥有的流程
     * @param pageParam 分页参数
     * @return 分页结果
     */
    @GetMapping(value = "/ownList")
    public CommonResult<PageResult<WorkflowTaskDTO>> ownProcess(PageParam pageParam) {
        return ResultGenerator.genOkResult(processService.queryPageOwnProcessList(pageParam));
    }

    /**
     * 获取待办列表
     * @param pageParam 分页参数
     * @return 分页结果
     */
    @GetMapping(value = "/todoList")
    public CommonResult<PageResult<WorkflowTaskDTO>> todoProcess(PageParam pageParam) {
        return ResultGenerator.genOkResult(processService.queryPageTodoProcessList(pageParam));
    }

    /**
     * 获取已办列表
     * @param pageParam 分页参数
     * @return 分页结果
     */
    @GetMapping(value = "/finishedList")
    public CommonResult<PageResult<WorkflowTaskDTO>> finishedProcess(PageParam pageParam) {
        return ResultGenerator.genOkResult(processService.queryPageFinishedProcessList(pageParam));
    }

}
