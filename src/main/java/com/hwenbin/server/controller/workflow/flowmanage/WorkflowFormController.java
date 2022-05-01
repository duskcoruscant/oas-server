package com.hwenbin.server.controller.workflow.flowmanage;

import cn.hutool.core.bean.BeanUtil;
import com.hwenbin.server.controller.workflow.flowmanage.req.AddWorkflowFormReq;
import com.hwenbin.server.controller.workflow.flowmanage.req.PageQueryForWorkflowFormReq;
import com.hwenbin.server.controller.workflow.flowmanage.req.UpdateWorkflowFormReq;
import com.hwenbin.server.core.web.response.CommonResult;
import com.hwenbin.server.core.web.response.PageResult;
import com.hwenbin.server.core.web.response.ResultGenerator;
import com.hwenbin.server.entity.WorkflowDeployFormEntity;
import com.hwenbin.server.entity.WorkflowFormEntity;
import com.hwenbin.server.service.WorkflowDeployFormService;
import com.hwenbin.server.service.WorkflowFormService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Arrays;

/**
 * 流程表单控制器
 *
 * @author hwb
 * @date 2022/04/29 18:54
 */
@RestController
@RequestMapping("/workflow/form")
public class WorkflowFormController {

    @Resource
    private WorkflowFormService workflowFormService;

    @Resource
    private WorkflowDeployFormService deployFormService;

    /**
     * 分页查询流程表单列表
     * @param req 查询参数 + 分页参数
     * @return 分页结果
     */
    @GetMapping("/list")
    public CommonResult<PageResult<WorkflowFormEntity>> pageQueryForWorkflowForm(
            @Valid PageQueryForWorkflowFormReq req) {
        return ResultGenerator.genOkResult(workflowFormService.pageQuery(req));
    }

    // /**
    //  * 导出流程表单列表
    //  */
    // @SaCheckPermission("workflow:form:export")
    // @Log(title = "流程表单", businessType = BusinessType.EXPORT)
    // @GetMapping("/export")
    // public void export(@Validated WfFormBo bo, HttpServletResponse response) {
    //     List<WfFormVo> list = formService.queryList(bo);
    //     ExcelUtil.exportExcel(list, "流程表单", WfFormVo.class, response);
    // }
    private String export;

    /**
     * 获取流程表单详细信息
     * @param formId 流程表单实体id
     * @return 流程表单实体
     */
    @GetMapping(value = "/{formId}")
    public CommonResult<WorkflowFormEntity> getInfo(
            @NotNull(message = "主键不能为空") @PathVariable("formId") Long formId) {
        return ResultGenerator.genOkResult(workflowFormService.getById(formId));
    }

    /**
     * 新增流程表单
     * @param req 流程表单参数
     * @return true
     */
    @PostMapping
    public CommonResult<Boolean> addWorkflowForm(@Valid @RequestBody AddWorkflowFormReq req) {
        WorkflowFormEntity formEntity = BeanUtil.toBean(req, WorkflowFormEntity.class);
        workflowFormService.add(formEntity);
        return ResultGenerator.genOkResult(true);
    }

    /**
     * 修改流程表单
     * @param req 流程表单参数
     * @return true
     */
    @PutMapping
    public CommonResult<Boolean> updateWorkflowForm(@Valid @RequestBody UpdateWorkflowFormReq req) {
        WorkflowFormEntity formEntity = BeanUtil.toBean(req, WorkflowFormEntity.class);
        workflowFormService.updateById(formEntity);
        return ResultGenerator.genOkResult(true);
    }

    /**
     * 删除流程表单
     * @param formIds 流程表单主键id数组
     * @return true
     */
    @DeleteMapping("/{formIds}")
    public CommonResult<Boolean> remove(@NotEmpty(message = "主键不能为空") @PathVariable Long[] formIds) {
        workflowFormService.deleteByIds(Arrays.asList(formIds));
        return ResultGenerator.genOkResult(true);
    }


    /**
     * 挂载流程表单
     */
    @PostMapping("/addDeployForm")
    public CommonResult<Void> addDeployForm(@RequestBody WorkflowDeployFormEntity deployForm) {
        deployFormService.insertWorkflowDeployForm(deployForm);
        return ResultGenerator.genOkResult();
    }

}
