package com.hwenbin.server.controller.managecenter;

import com.hwenbin.server.controller.managecenter.req.GetAllDeptListReq;
import com.hwenbin.server.core.web.response.CommonResult;
import com.hwenbin.server.core.web.response.ResultCode;
import com.hwenbin.server.core.web.response.ResultGenerator;
import com.hwenbin.server.dto.DepartmentDTO;
import com.hwenbin.server.entity.Department;
import com.hwenbin.server.service.DepartmentService;
import com.hwenbin.server.util.AssertUtils;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.security.Principal;
import java.util.List;

/**
 * @author hwb
 * @create 2022-03-25
 */
@RestController
@RequestMapping("/department")
public class DepartmentController {

    @Resource
    private DepartmentService departmentService;

    /**
     * 所有部门列表
     * @param req 筛选条件
     * @return 部门列表
     */
    @PreAuthorize("hasAuthority('manage:department:query')")
    @GetMapping
    public CommonResult<List<Department>> getAllDeptList(final GetAllDeptListReq req) {
        return ResultGenerator.genOkResult(departmentService.getAllDeptList(req));
    }

    /**
     * 获取所有状态为 enable 的部门列表
     * @return enable 部门列表
     */
    @GetMapping("/listEnableDept")
    public CommonResult<List<Department>> getEnableDeptList() {
        return ResultGenerator.genOkResult(departmentService.getEnableDeptList());
    }

    /**
     * 新增部门
     * @param departmentDTO 部门信息
     * @param principal 登录信息
     * @return true
     */
    @PreAuthorize("hasAuthority('manage:department:add')")
    @PostMapping()
    public CommonResult<Boolean> add(@Valid @RequestBody final DepartmentDTO departmentDTO, final Principal principal) {
        departmentService.addDept(departmentDTO);
        return ResultGenerator.genOkResult(true);
    }

    /**
     * 获取部门详细信息
     * @param id 部门id
     * @return 部门信息
     */
    @PreAuthorize("hasAuthority('manage:department:query')")
    @GetMapping("/get")
    public CommonResult<Department> get(@RequestParam("id") final Long id) {
        Department department = departmentService.getById(id);
        AssertUtils.asserts(department != null, ResultCode.DEPT_NOT_FOUND);
        return ResultGenerator.genOkResult(department);
    }

    /**
     * 更新部门信息
     * @param departmentDTO 要更新的信息
     * @param principal 登录信息
     * @return true
     */
    @PreAuthorize("hasAuthority('manage:department:update')")
    @PutMapping
    public CommonResult<Boolean> update(@Valid @RequestBody final DepartmentDTO departmentDTO, final Principal principal) {
        departmentService.updateDept(departmentDTO);
        return ResultGenerator.genOkResult(true);
    }

    /**
     * 删除部门
     * @param id 部门id
     * @param principal 登录信息
     * @return true
     */
    @PreAuthorize("hasAuthority('manage:department:delete')")
    @DeleteMapping
    public CommonResult<Boolean> delete(@RequestParam final Long id, final Principal principal) {
        departmentService.deleteDept(id);
        return ResultGenerator.genOkResult(true);
    }

}
