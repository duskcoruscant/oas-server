package com.hwenbin.server.controller.workflow.workmanage;

import cn.hutool.core.util.ObjectUtil;
import com.hwenbin.server.core.web.response.CommonResult;
import com.hwenbin.server.core.web.response.PageParam;
import com.hwenbin.server.core.web.response.PageResult;
import com.hwenbin.server.core.web.response.ResultGenerator;
import com.hwenbin.server.dto.flow.WorkflowNextDTO;
import com.hwenbin.server.dto.flow.WorkflowTaskDTO;
import com.hwenbin.server.dto.flow.WorkflowViewerDTO;
import com.hwenbin.server.entity.WorkflowTaskEntity;
import com.hwenbin.server.service.WorkflowTaskService;
import org.flowable.bpmn.model.UserTask;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

/**
 * 工作流任务管理
 *
 * @author hwb
 * @date 2022/05/02 21:30
 */
@RestController
@RequestMapping("/workflow/task")
public class WorkflowTaskController {

    @Resource
    private WorkflowTaskService flowTaskService;

    /**
     * 取消申请 TODO:点击取消申请为什么显示通过了
     * @param bo
     * @return
     */
    @PostMapping(value = "/stopProcess")
    public CommonResult<Void> stopProcess(@RequestBody WorkflowTaskEntity bo) {
        flowTaskService.stopProcess(bo);
        return ResultGenerator.genOkResult();
    }

    /**
     * 撤回流程
     * @param bo
     * @return
     */
    @PostMapping(value = "/revokeProcess")
    public CommonResult<Void> revokeProcess(@RequestBody WorkflowTaskEntity bo) {
        flowTaskService.revokeProcess(bo);
        return ResultGenerator.genOkResult();
    }

    /**
     * 获取待办列表
     * @param pageParam 分页参数
     * @return 分页结果
     */
    @Deprecated
    @GetMapping(value = "/todoList")
    public CommonResult<PageResult<WorkflowTaskDTO>> todoList(PageParam pageParam) {
        return ResultGenerator.genOkResult(flowTaskService.todoList(pageParam));
    }

    /**
     * 获取已办任务
     * @param pageParam 分页参数
     * @return 分页结果
     */
    @Deprecated
    @GetMapping(value = "/finishedList")
    public CommonResult<PageResult<WorkflowTaskDTO>> finishedList(PageParam pageParam) {
        return ResultGenerator.genOkResult(flowTaskService.finishedList(pageParam));
    }

    /**
     * 获取流程变量
     * @param taskId 流程任务Id
     * @return 流程变量
     */
    @GetMapping(value = "/processVariables/{taskId}")
    public CommonResult<?> processVariables(@PathVariable(value = "taskId") String taskId) {
        return ResultGenerator.genOkResult(flowTaskService.getProcessVariables(taskId));
    }

    /**
     * 审批任务
     * @param bo
     * @return
     */
    @PostMapping(value = "/complete")
    public CommonResult<Void> complete(@RequestBody WorkflowTaskEntity bo) {
        flowTaskService.complete(bo);
        return ResultGenerator.genOkResult();
    }


    /**
     * 驳回任务
     * @param bo
     * @return
     */
    @PostMapping(value = "/reject")
    public CommonResult<Void> taskReject(@RequestBody WorkflowTaskEntity bo) {
        flowTaskService.taskReject(bo);
        return ResultGenerator.genOkResult();
    }

    /**
     * 退回任务
     * @param bo
     * @return
     */
    @PostMapping(value = "/return")
    public CommonResult<Void> taskReturn(@RequestBody WorkflowTaskEntity bo) {
        flowTaskService.taskReturn(bo);
        return ResultGenerator.genOkResult();
    }

    /**
     * 获取所有可回退的节点
     * @param bo
     * @return
     */
    @PostMapping(value = "/returnList")
    public CommonResult<List<UserTask>> findReturnTaskList(@RequestBody WorkflowTaskEntity bo) {
        return ResultGenerator.genOkResult(flowTaskService.findReturnTaskList(bo));
    }

    /**
     * 删除任务
     * @param bo
     * @return
     */
    @DeleteMapping(value = "/delete")
    public CommonResult<Void> delete(@RequestBody WorkflowTaskEntity bo) {
        flowTaskService.deleteTask(bo);
        return ResultGenerator.genOkResult();
    }

    /**
     * 认领/签收任务
     * @param bo
     * @return
     */
    @PostMapping(value = "/claim")
    public CommonResult<Void> claim(@RequestBody WorkflowTaskEntity bo) {
        flowTaskService.claim(bo);
        return ResultGenerator.genOkResult();
    }

    /**
     * 取消认领/签收任务
     * @param bo
     * @return
     */
    @PostMapping(value = "/unClaim")
    public CommonResult<Void> unClaim(@RequestBody WorkflowTaskEntity bo) {
        flowTaskService.unClaim(bo);
        return ResultGenerator.genOkResult();
    }

    /**
     * 委派任务
     * @param bo
     * @return
     */
    @PostMapping(value = "/delegate")
    public CommonResult<Void> delegate(@RequestBody WorkflowTaskEntity bo) {
        if (ObjectUtil.hasNull(bo.getTaskId(), bo.getUserId())) {
            return ResultGenerator.genFailedResult("参数错误！");
        }
        flowTaskService.delegateTask(bo);
        return ResultGenerator.genOkResult();
    }

    /**
     * 转办任务
     * @param bo
     * @return
     */
    @PostMapping(value = "/transfer")
    public CommonResult<Void> transfer(@RequestBody WorkflowTaskEntity bo) {
        if (ObjectUtil.hasNull(bo.getTaskId(), bo.getUserId())) {
            return ResultGenerator.genFailedResult("参数错误！");
        }
        flowTaskService.transferTask(bo);
        return ResultGenerator.genOkResult();
    }

    /**
     * 获取下一节点
     * @param bo
     * @return
     */
    @PostMapping(value = "/nextFlowNode")
    public CommonResult<?> getNextFlowNode(@RequestBody WorkflowTaskEntity bo) {
        WorkflowNextDTO wfNextDto = flowTaskService.getNextFlowNode(bo);
        return wfNextDto != null ? ResultGenerator.genOkResult(wfNextDto)
                : ResultGenerator.genOkResult(null, "流程已完结");
    }

    /**
     * 生成流程图
     *
     * @param processId 任务ID
     */
    @RequestMapping("/diagram/{processId}")
    public void genProcessDiagram(HttpServletResponse response,
                                  @PathVariable("processId") String processId) {
        InputStream inputStream = flowTaskService.diagram(processId);
        OutputStream os = null;
        BufferedImage image;
        try {
            image = ImageIO.read(inputStream);
            response.setContentType("image/png");
            os = response.getOutputStream();
            if (image != null) {
                ImageIO.write(image, "png", os);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (os != null) {
                    os.flush();
                    os.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 生成流程图
     *
     * @param procInsId 任务ID
     */
    @RequestMapping("/flowViewer/{procInsId}")
    public CommonResult<WorkflowViewerDTO> getFlowViewer(@PathVariable("procInsId") String procInsId) {
        return ResultGenerator.genOkResult(flowTaskService.getFlowViewer(procInsId));
    }

}
