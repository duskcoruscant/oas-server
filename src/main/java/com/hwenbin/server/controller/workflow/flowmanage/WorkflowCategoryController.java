package com.hwenbin.server.controller.workflow.flowmanage;

import cn.hutool.core.bean.BeanUtil;
import com.hwenbin.server.controller.workflow.flowmanage.req.AddWorkflowCategoryReq;
import com.hwenbin.server.controller.workflow.flowmanage.req.PageQueryForWorkflowCategoryReq;
import com.hwenbin.server.controller.workflow.flowmanage.req.UpdateWorkflowCategoryReq;
import com.hwenbin.server.core.web.response.CommonResult;
import com.hwenbin.server.core.web.response.PageResult;
import com.hwenbin.server.core.web.response.ResultGenerator;
import com.hwenbin.server.entity.WorkflowCategoryEntity;
import com.hwenbin.server.service.WorkflowCategoryService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Arrays;

/**
 * 流程分类控制器
 *
 * @author hwb
 * @date 2022/04/29 14:01
 */
@RestController
@RequestMapping("/workflow/category")
public class WorkflowCategoryController {

    @Resource
    private WorkflowCategoryService workflowCategoryService;

    /**
     * 分页查询流程分类列表
     * @param req 查询参数 + 分页参数
     * @return 分页结果
     */
    @GetMapping("/list")
    public CommonResult<PageResult<WorkflowCategoryEntity>> pageQueryForWorkflowCategory(
            @Valid PageQueryForWorkflowCategoryReq req) {
        return ResultGenerator.genOkResult(workflowCategoryService.pageQuery(req));
    }

    // /**
    //  * 导出流程分类列表
    //  * @param bo
    //  * @param response
    //  */
    // @PostMapping("/export")
    // public void export(@Validated WfCategoryBo bo, HttpServletResponse response) {
    //     List<WfCategoryVo> list = flowCategoryService.queryList(bo);
    //     ExcelUtil.exportExcel(list, "流程分类", WfCategoryVo.class, response);
    // }
    private String export;

    /**
     * 获取流程分类详细信息
     * @param categoryId 流程分类实体id
     * @return 流程分类实体
     */
    @GetMapping("/{categoryId}")
    public CommonResult<WorkflowCategoryEntity> getInfo(
            @NotNull(message = "主键不能为空") @PathVariable("categoryId") Long categoryId) {
        return ResultGenerator.genOkResult(workflowCategoryService.getById(categoryId));
    }

    /**
     * 新增流程分类
     * @param req 流程分类参数
     * @return true
     */
    @PostMapping
    public CommonResult<Boolean> addWorkflowCategory(@Valid @RequestBody AddWorkflowCategoryReq req) {
        WorkflowCategoryEntity categoryEntity = BeanUtil.toBean(req, WorkflowCategoryEntity.class);
        workflowCategoryService.add(categoryEntity);
        return ResultGenerator.genOkResult(true);
    }

    /**
     * 修改流程分类
     * @param req 流程分类参数
     * @return true
     */
    @PutMapping
    public CommonResult<Boolean> updateWorkflowCategory(@Valid @RequestBody UpdateWorkflowCategoryReq req) {
        WorkflowCategoryEntity categoryEntity = BeanUtil.toBean(req, WorkflowCategoryEntity.class);
        workflowCategoryService.updateById(categoryEntity);
        return ResultGenerator.genOkResult(true);
    }

    /**
     * 删除流程分类
     * @param categoryIds 流程分类主键id数组
     * @return true
     */
    @DeleteMapping("/{categoryIds}")
    public CommonResult<Boolean> remove(@NotEmpty(message = "主键不能为空") @PathVariable Long[] categoryIds) {
        workflowCategoryService.deleteByIds(Arrays.asList(categoryIds));
        return ResultGenerator.genOkResult(true);
    }

}
