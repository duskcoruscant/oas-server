package com.hwenbin.server.controller.managecenter;

import com.hwenbin.server.controller.managecenter.req.AddEmployeeReq;
import com.hwenbin.server.controller.managecenter.req.GetEmployeeListReq;
import com.hwenbin.server.controller.managecenter.req.UpdateEmpStatusReq;
import com.hwenbin.server.controller.managecenter.req.UpdateEmployeeReq;
import com.hwenbin.server.core.web.response.CommonResult;
import com.hwenbin.server.core.web.response.PageResult;
import com.hwenbin.server.core.web.response.ResultGenerator;
import com.hwenbin.server.dto.EmployeeDTO;
import com.hwenbin.server.entity.Employee;
import com.hwenbin.server.service.EmployeeService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.security.Principal;
import java.util.List;

/**
 * @author hwb
 * @create 2022-03-22
 */
@RestController
@RequestMapping("/employee")
public class EmployeeController {

    @Resource
    private EmployeeService employeeService;

    /**
     * 添加新员工
     * @param req 参数
     * @param principal 登录信息
     * @return true
     */
    @PreAuthorize("hasAuthority('manage:employee:add')")
    @PostMapping
    public CommonResult<Boolean> add(@Valid @RequestBody final AddEmployeeReq req, final Principal principal) {
        employeeService.addEmployee(req);
        return ResultGenerator.genOkResult(true);
    }

    /**
     * 获取员工列表
     * @param req 分页参数 + 筛选条件
     * @return 分页结果
     */
    @PreAuthorize("hasAuthority('manage:employee:query')")
    @GetMapping
    public CommonResult<PageResult<EmployeeDTO>> pageQuery(@Valid final GetEmployeeListReq req) {
        return ResultGenerator.genOkResult(employeeService.pageQuery(req));
    }

    /**
     * 展示所有 状态为 Enable 的员工，用于下拉框选择
     * @return enable员工列表
     */
    @GetMapping("/listEnableEmp")
    public CommonResult<List<Employee>> getEnableEmpList() {
        return ResultGenerator.genOkResult(employeeService.getEnableEmpList());
    }

    /**
     * 更新员工信息
     * @param req 参数
     * @param principal 登录信息
     * @return true
     */
    @PreAuthorize("hasAuthority('manage:employee:update')")
    @PutMapping
    public CommonResult<Boolean> update(@Valid @RequestBody final UpdateEmployeeReq req, final Principal principal) {
        employeeService.updateEmployee(req);
        return ResultGenerator.genOkResult(true);
    }

    /**
     * 通过id删除员工
     * @param id 员工id
     * @param principal 登录信息
     * @return true
     */
    @PreAuthorize("hasAuthority('manage:employee:delete')")
    @DeleteMapping("/{id}")
    public CommonResult<Boolean> deleteById(@PathVariable final Long id, final Principal principal) {
        employeeService.deleteEmployee(id);
        return ResultGenerator.genOkResult(true);
    }

    /**
     * 获取员工信息
     * @param id 员工id
     * @param principal 登录信息
     * @return 员工信息
     */
    @PreAuthorize("hasAuthority('manage:employee:query')")
    @GetMapping("/get")
    public CommonResult<Employee> getEmpDetail(@RequestParam final Long id, final Principal principal) {
        return ResultGenerator.genOkResult(employeeService.getEmployeeById(id));
    }

    /**
     * 更新员工状态
     * @param req 参数
     * @return true
     */
    @PreAuthorize("hasAuthority('manage:employee:update')")
    @PutMapping("/updateStatus")
    public CommonResult<Boolean> updateEmpStatus(@Valid @RequestBody final UpdateEmpStatusReq req) {
        employeeService.updateEmpStatus(req.getId(), req.getStatus());
        return ResultGenerator.genOkResult(true);
    }

}
