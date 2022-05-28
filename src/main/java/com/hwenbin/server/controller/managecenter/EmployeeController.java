package com.hwenbin.server.controller.managecenter;

import com.hwenbin.server.controller.managecenter.req.*;
import com.hwenbin.server.controller.managecenter.resp.ImportExcelEmployeesResp;
import com.hwenbin.server.core.web.response.CommonResult;
import com.hwenbin.server.core.web.response.PageResult;
import com.hwenbin.server.core.web.response.ResultGenerator;
import com.hwenbin.server.dto.EmployeeDTO;
import com.hwenbin.server.entity.Employee;
import com.hwenbin.server.service.EmployeeService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
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

    /**
     * 展示id员工所在部门所有 状态为 Enable 的员工，用于下拉框选择
     * @param empId 员工id
     * @return 员工实体列表
     */
    @GetMapping("/listDeptEmpByEmpId/{empId}")
    public CommonResult<List<Employee>> getDeptEnableEmpListByEmpId(@PathVariable Long empId) {
        return ResultGenerator.genOkResult(employeeService.getDeptEnableEmpListByEmpId(empId));
    }

    /**
     * 导出员工列表
     * @param req 员工筛选参数，与 GetEmployeeListReq 一致
     * @param httpServletResponse 响应
     * @throws IOException io
     */
    @GetMapping("/export")
    public void exportExcelEmployees(@Valid ExportEmployeesReq req, HttpServletResponse httpServletResponse)
            throws IOException {
        employeeService.exportEmployees(req, httpServletResponse);
    }

    /**
     * 获取员工导入模板
     * @param httpServletResponse 响应
     * @throws IOException io
     */
    @GetMapping("/getImportTemplate")
    public void getImportTemplate(HttpServletResponse httpServletResponse) throws IOException {
        employeeService.getImportTemplate(httpServletResponse);
    }

    /**
     * 导入员工
     * @param file Excel 文件
     * @param updateSupport 是否支持更新，默认为 false
     * @return 统计
     * @throws Exception ex
     */
    @PostMapping("/import")
    public CommonResult<ImportExcelEmployeesResp> importExcelEmployees(
            @RequestParam("file") MultipartFile file,
            @RequestParam(value = "updateSupport", required = false, defaultValue = "false") Boolean updateSupport)
            throws Exception {
        return ResultGenerator.genOkResult(employeeService.importEmployees(file, updateSupport));
    }

}
