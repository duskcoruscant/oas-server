package com.hwenbin.server.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hwenbin.server.controller.managecenter.req.AddEmployeeReq;
import com.hwenbin.server.controller.managecenter.req.ExportEmployeesReq;
import com.hwenbin.server.controller.managecenter.req.GetEmployeeListReq;
import com.hwenbin.server.controller.managecenter.req.UpdateEmployeeReq;
import com.hwenbin.server.controller.managecenter.resp.ImportExcelEmployeesResp;
import com.hwenbin.server.core.web.response.PageResult;
import com.hwenbin.server.dto.EmployeeDTO;
import com.hwenbin.server.entity.Employee;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Set;

/**
 * @author hwb
 * @create 2022-03-22
 */
public interface EmployeeService extends IService<Employee> {

    /**
     * 获取员工列表
     * @param req 分页 + 筛选
     * @return 分页结果
     */
    PageResult<EmployeeDTO> pageQuery(GetEmployeeListReq req);

    /**
     * 更新员工状态
     * @param id 员工id
     * @param status 员工状态
     */
    void updateEmpStatus(Long id, Integer status);

    /**
     * 添加新员工
     * @param req 新员工信息
     */
    void addEmployee(AddEmployeeReq req);

    /**
     * 展示所有 状态为 Enable 的员工
     * @return enable的员工列表
     */
    List<Employee> getEnableEmpList();

    /**
     * 更新员工信息
     * @param req 员工信息参数
     */
    void updateEmployee(UpdateEmployeeReq req);

    /**
     * 删除员工
     * @param id 员工id
     */
    void deleteEmployee(Long id);

    /**
     * 获取指定员工信息
     * @param id 员工id
     * @return 员工信息
     */
    Employee getEmployeeById(Long id);

    /**
     * 展示id员工所在部门下所有 状态为 Enable 的员工
     * @param empId 员工id
     * @return 员工实体列表
     */
    List<Employee> getDeptEnableEmpListByEmpId(Long empId);

    /**
     * 根据deptId获取所有员工id
     * @param deptIds 部门id列表
     * @return 员工id集合
     */
    Set<Long> getAllEmpIdByDeptId(List<Long> deptIds);

    /**
     * 导出员工列表
     * @param req 员工筛选参数，与 GetEmployeeListReq 一致
     * @param httpServletResponse 响应
     * @throws IOException io
     */
    void exportEmployees(ExportEmployeesReq req, HttpServletResponse httpServletResponse) throws IOException;

    /**
     * 获取员工导入模板
     * @param httpServletResponse 响应
     * @throws IOException io
     */
    void getImportTemplate(HttpServletResponse httpServletResponse) throws IOException;

    /**
     * 导入员工
     * @param file Excel 文件
     * @param updateSupport 是否支持更新，默认为 false
     * @return 统计
     * @throws IOException io
     */
    ImportExcelEmployeesResp importEmployees(MultipartFile file, Boolean updateSupport) throws IOException;

}
