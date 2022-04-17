package com.hwenbin.server.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hwenbin.server.controller.managecenter.req.AddEmployeeReq;
import com.hwenbin.server.controller.managecenter.req.GetEmployeeListReq;
import com.hwenbin.server.controller.managecenter.req.UpdateEmployeeReq;
import com.hwenbin.server.core.mybatis.query.MyLambdaQueryWrapper;
import com.hwenbin.server.core.web.response.PageResult;
import com.hwenbin.server.core.web.response.ResultCode;
import com.hwenbin.server.dto.AccountDTO;
import com.hwenbin.server.dto.EmployeeDTO;
import com.hwenbin.server.entity.Account;
import com.hwenbin.server.entity.Employee;
import com.hwenbin.server.enums.CommonStatusEnum;
import com.hwenbin.server.mapper.EmployeeMapper;
import com.hwenbin.server.service.AccountService;
import com.hwenbin.server.service.DepartmentService;
import com.hwenbin.server.service.EmployeeService;
import com.hwenbin.server.service.PositionService;
import com.hwenbin.server.util.AssertUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author hwb
 * @create 2022-03-22
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class EmployeeServiceImpl extends ServiceImpl<EmployeeMapper, Employee> implements EmployeeService {

    @Resource
    private EmployeeMapper employeeMapper;

    @Resource
    private DepartmentService departmentService;

    @Resource
    private AccountService accountService;

    @Resource
    private PositionService positionService;

    @Override
    public PageResult<EmployeeDTO> pageQuery(GetEmployeeListReq req) {
        final Set<Long> deptIds;
        if (req.getDeptId() != null) {
            deptIds = departmentService.getDeptIdListWithItAndChildId(req.getDeptId());
        } else {
            deptIds = null;
        }
        PageResult<Employee> pageResult = employeeMapper.selectPage(req,
                new MyLambdaQueryWrapper<Employee>()
                        .likeIfPresent(Employee::getName, req.getName())
                        .likeIfPresent(Employee::getPhone, req.getPhone())
                        .eqIfPresent(Employee::getStatus, req.getStatus())
                        .betweenIfPresent(Employee::getEntryDate, req.getBeginTime(), req.getEndTime())
                        .inIfPresent(Employee::getDeptId, deptIds)
        );
        List<Employee> list = pageResult.getList();
        // 部门id列表
        List<Long> respDeptIds = list.stream().map(Employee::getDeptId).collect(Collectors.toList());
        Map<Long, String> deptMap = departmentService.getDeptMapByIds(respDeptIds);
        // 职位id列表
        Set<Long> positionIds = new HashSet<>();
        list.stream().map(Employee::getPositionIds).filter(CollUtil::isNotEmpty).forEach(positionIds::addAll);
        Map<Long, String> positionMap = positionService.getPositionMapByIds(positionIds);
        return new PageResult<>(
                list.stream().map(employee -> {
                    final EmployeeDTO dto = new EmployeeDTO();
                    BeanUtil.copyProperties(employee, dto);
                    // 部门名称
                    dto.setDeptName(deptMap.get(dto.getDeptId()));
                    // 职位名称
                    if (CollUtil.isNotEmpty(dto.getPositionIds())) {
                        dto.setPositionName(
                                dto.getPositionIds().stream().map(positionMap::get).collect(Collectors.joining(","))
                        );
                    }
                    return dto;
                }).collect(Collectors.toList()), pageResult.getTotal()
        );
    }

    @Override
    public void updateEmpStatus(Long id, Integer status) {
        // TODO ： 校验员工是否存在
        Employee employee = new Employee();
        employee.setId(id);
        employee.setStatus(status);
        employeeMapper.updateById(employee);
    }

    @Override
    public void addEmployee(AddEmployeeReq req) {
        Employee employee = new Employee();
        BeanUtil.copyProperties(req, employee);
        employee.setStatus(CommonStatusEnum.ENABLE.getStatus());
        employeeMapper.insert(employee);
        // 创建账号
        AccountDTO accountDto = new AccountDTO();
        BeanUtil.copyProperties(req, accountDto);
        // 设置 员工id
        accountDto.setId(employee.getId());
        accountService.register(accountDto);
    }

    @Override
    public List<Employee> getEnableEmpList() {
        return employeeMapper.selectList("status", CommonStatusEnum.ENABLE.getStatus());
    }

    @Override
    public void updateEmployee(UpdateEmployeeReq req) {
        final Employee dbEmployee = employeeMapper.selectById(req.getId());
        AssertUtils.asserts(dbEmployee != null, ResultCode.EMP_NOT_FOUND);
        final Employee updateEmployee = new Employee();
        BeanUtil.copyProperties(req, updateEmployee);
        employeeMapper.updateById(updateEmployee);
        // 如果Email变更，需要更新账号表
        if (updateEmployee.getEmail() != null) {
            final Account account = new Account();
            account.setId(updateEmployee.getId());
            account.setEmail(updateEmployee.getEmail());
            accountService.updateById(account);
        }
    }

    @Override
    public void deleteEmployee(Long id) {
        final Employee dbEmployee = employeeMapper.selectById(id);
        AssertUtils.asserts(dbEmployee != null, ResultCode.EMP_NOT_FOUND);
        employeeMapper.deleteById(id);
        // TODO ：关联账户的处理   考虑不删除账户，但是删除账户与角色关联记录
    }

    @Override
    public Employee getEmployeeById(Long id) {
        final Employee dbEmployee = employeeMapper.selectById(id);
        AssertUtils.asserts(dbEmployee != null, ResultCode.EMP_NOT_FOUND);
        return dbEmployee;
    }

    @Override
    public List<Employee> getDeptEnableEmpListByEmpId(Long empId) {
        Employee employee = employeeMapper.selectById(empId);
        List<Employee> list = employeeMapper.selectList(
                new MyLambdaQueryWrapper<Employee>()
                        .eq(Employee::getDeptId, employee.getDeptId())
                        .eq(Employee::getStatus, CommonStatusEnum.ENABLE.getStatus())
        );
        // 过滤掉自己
        list.remove(employee);
        return list;
    }

}
