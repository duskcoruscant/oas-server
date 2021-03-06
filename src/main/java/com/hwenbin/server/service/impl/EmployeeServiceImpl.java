package com.hwenbin.server.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hwenbin.server.controller.managecenter.req.AddEmployeeReq;
import com.hwenbin.server.controller.managecenter.req.ExportEmployeesReq;
import com.hwenbin.server.controller.managecenter.req.GetEmployeeListReq;
import com.hwenbin.server.controller.managecenter.req.UpdateEmployeeReq;
import com.hwenbin.server.controller.managecenter.resp.ImportExcelEmployeesResp;
import com.hwenbin.server.core.exception.ServiceException;
import com.hwenbin.server.core.mybatis.query.MyLambdaQueryWrapper;
import com.hwenbin.server.core.web.response.PageResult;
import com.hwenbin.server.core.web.response.ResultCode;
import com.hwenbin.server.dto.AccountDTO;
import com.hwenbin.server.dto.EmployeeDTO;
import com.hwenbin.server.dto.excel.EmployeeExportExcelDTO;
import com.hwenbin.server.dto.excel.EmployeeImportExcelDTO;
import com.hwenbin.server.entity.Account;
import com.hwenbin.server.entity.Employee;
import com.hwenbin.server.enums.CommonStatusEnum;
import com.hwenbin.server.mapper.EmployeeMapper;
import com.hwenbin.server.service.AccountService;
import com.hwenbin.server.service.DepartmentService;
import com.hwenbin.server.service.EmployeeService;
import com.hwenbin.server.service.PositionService;
import com.hwenbin.server.util.AssertUtils;
import com.hwenbin.server.util.DateUtils;
import com.hwenbin.server.util.ExcelUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;
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
        // ??????id??????
        List<Long> respDeptIds = list.stream().map(Employee::getDeptId).collect(Collectors.toList());
        Map<Long, String> deptMap = departmentService.getDeptMapByIds(respDeptIds);
        // ??????id??????
        Set<Long> positionIds = new HashSet<>();
        list.stream().map(Employee::getPositionIds).filter(CollUtil::isNotEmpty).forEach(positionIds::addAll);
        Map<Long, String> positionMap = positionService.getPositionMapByIds(positionIds);
        return new PageResult<>(
                list.stream().map(employee -> {
                    final EmployeeDTO dto = new EmployeeDTO();
                    BeanUtil.copyProperties(employee, dto);
                    // ????????????
                    dto.setDeptName(deptMap.get(dto.getDeptId()));
                    // ????????????
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
        // TODO ??? ????????????????????????
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
        // ????????????
        AccountDTO accountDto = new AccountDTO();
        BeanUtil.copyProperties(req, accountDto);
        // ?????? ??????id
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
        // ??????Email??????????????????????????????
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
        // TODO ????????????????????????   ???????????????????????????????????????????????????????????????
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
        // ???????????????
        list.remove(employee);
        return list;
    }

    @Override
    public Set<Long> getAllEmpIdByDeptId(List<Long> deptIds) {
        return employeeMapper.selectList(
                new MyLambdaQueryWrapper<Employee>()
                        .select(Employee::getId)
                        .in(Employee::getDeptId, deptIds)
        ).stream().map(Employee::getId).collect(Collectors.toSet());
    }

    @Override
    public void exportEmployees(ExportEmployeesReq req, HttpServletResponse httpServletResponse) throws IOException {
        // ??????????????????id??????????????????id??????
        final Set<Long> deptIds;
        if (req.getDeptId() != null) {
            deptIds = departmentService.getDeptIdListWithItAndChildId(req.getDeptId());
        } else {
            deptIds = null;
        }
        // ??????????????????
        List<Employee> employees = employeeMapper.selectList(
                new MyLambdaQueryWrapper<Employee>()
                        .likeIfPresent(Employee::getName, req.getName())
                        .likeIfPresent(Employee::getPhone, req.getPhone())
                        .eqIfPresent(Employee::getStatus, req.getStatus())
                        .betweenIfPresent(Employee::getEntryDate, req.getBeginTime(), req.getEndTime())
                        .inIfPresent(Employee::getDeptId, deptIds)
        );
        // ??????????????????map
        Map<Long, String> deptMap = departmentService.getDeptMapByIds(
                employees.stream().map(Employee::getDeptId).collect(Collectors.toSet())
        );
        // ??????????????????map
        Set<Long> positionIds = new HashSet<>();
        employees.stream().map(Employee::getPositionIds).filter(CollUtil::isNotEmpty).forEach(positionIds::addAll);
        Map<Long, String> positionMap = positionService.getPositionMapByIds(positionIds);
        // ????????????
        List<EmployeeExportExcelDTO> excelEmployees = new ArrayList<>(employees.size());
        employees.forEach(employee -> {
            EmployeeExportExcelDTO dto = BeanUtil.toBean(employee, EmployeeExportExcelDTO.class);
            // ??????????????????
            dto.setDeptName(deptMap.get(employee.getDeptId()));
            // ??????????????????
            if (CollUtil.isNotEmpty(employee.getPositionIds())) {
                dto.setPositions(
                        employee.getPositionIds()
                                .stream().map(positionMap::get).collect(Collectors.joining(","))
                );
            }
            excelEmployees.add(dto);
        });
        // ??????
        ExcelUtils.write(httpServletResponse, "????????????.xlsx", "????????????",
                EmployeeExportExcelDTO.class, excelEmployees);
    }

    @Override
    public void getImportTemplate(HttpServletResponse httpServletResponse) throws IOException {
        // ???????????? demo
        List<EmployeeImportExcelDTO> importExcelEmployees = Collections.singletonList(
                EmployeeImportExcelDTO.builder()
                        .name("hwb").nickname("hwb").password("123456").sex(2).phone("13350396869")
                        .email("hwb123@163.com").birthday(DateUtils.getNowDate()).positionIds("7")
                        .deptId(116L).entryDate(DateUtils.getNowDate()).remark("??????").status(0)
                        .build()
        );
        // ??????
        ExcelUtils.write(httpServletResponse, "??????????????????.xls", "????????????",
                EmployeeImportExcelDTO.class, importExcelEmployees);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ImportExcelEmployeesResp importEmployees(MultipartFile file, Boolean updateSupport) throws IOException {
        List<EmployeeImportExcelDTO> importEmployees = ExcelUtils.read(file, EmployeeImportExcelDTO.class);
        // ????????????????????????
        AssertUtils.asserts(CollUtil.isNotEmpty(importEmployees), ResultCode.EMP_IMPORT_LIST_IS_EMPTY);
        // ????????????
        ImportExcelEmployeesResp statisticResp = ImportExcelEmployeesResp.builder().createNicknames(new ArrayList<>())
                .updateNicknames(new ArrayList<>()).failureNicknames(new LinkedHashMap<>()).build();
        importEmployees.forEach(importEmployee -> {
            Set<Long> positionsIds =
                    Arrays.stream(ArrayUtil.wrap(StrUtil.splitToLong(importEmployee.getPositionIds(), ',')))
                            .collect(Collectors.toSet());
            // ?????????????????????????????????????????? TODO: ??????????????????????????????
            try {
                checkCreateOrUpdate(null, null, importEmployee.getPhone(), importEmployee.getEmail(),
                        importEmployee.getDeptId(), positionsIds);
            } catch (ServiceException ex) {
                statisticResp.getFailureNicknames().put(importEmployee.getNickname(), ex.getMessage());
                return;
            }
            // ???????????????????????????????????????
            Employee existEmployee = employeeMapper.selectOne(Employee::getNickname, importEmployee.getNickname());
            if (existEmployee == null) {
                AddEmployeeReq addEmployeeReq = BeanUtil.toBean(importEmployee, AddEmployeeReq.class);
                addEmployeeReq.setPositionIds(positionsIds);
                addEmployee(addEmployeeReq);
                statisticResp.getCreateNicknames().add(importEmployee.getNickname());
                return;
            }
            // ???????????????????????????????????????
            if (!updateSupport) {
                statisticResp.getFailureNicknames()
                        .put(importEmployee.getNickname(), ResultCode.EMP_NICKNAME_DUPLICATE.getReason());
                return;
            }
            Employee updateEmployee = BeanUtil.toBean(importEmployee, Employee.class);
            updateEmployee.setPositionIds(positionsIds);
            updateEmployee.setId(existEmployee.getId());
            employeeMapper.updateById(updateEmployee);
            statisticResp.getUpdateNicknames().add(importEmployee.getNickname());
        });
        return statisticResp;
    }

    private void checkCreateOrUpdate(Long id, String nickname, String phone, String email,
                                     Long deptId, Set<Long> postIds) {
        // ??????????????????
        this.checkEmployeeExists(id);
        // ????????????????????????
        this.checkNicknameUnique(id, nickname);
        // ?????????????????????
        this.checkPhoneUnique(id, phone);
        // ??????????????????
        this.checkEmailUnique(id, email);
        // ?????????????????????????????? TODO
        // departmentService.validDepts(CollectionUtils.singleton(deptId));
        // ?????????????????????????????? TODO
        // positionService.validPosts(postIds);
    }

    public void checkEmployeeExists(Long id) {
        if (id == null) {
            return;
        }
        Employee employee = employeeMapper.selectById(id);
        AssertUtils.asserts(employee != null, ResultCode.EMP_NOT_FOUND);
    }

    public void checkNicknameUnique(Long id, String nickname) {
        if (StrUtil.isBlank(nickname)) {
            return;
        }
        Employee employee = employeeMapper.selectOne(Employee::getNickname, nickname);
        if (employee == null) {
            return;
        }
        // ?????? id ?????????????????????????????????????????? id ?????????
        AssertUtils.asserts(id != null, ResultCode.EMP_NICKNAME_DUPLICATE);
        AssertUtils.asserts(employee.getId().equals(id), ResultCode.EMP_NICKNAME_DUPLICATE);
    }

    public void checkPhoneUnique(Long id, String phone) {
        if (StrUtil.isBlank(phone)) {
            return;
        }
        Employee employee = employeeMapper.selectOne(Employee::getPhone, phone);
        if (employee == null) {
            return;
        }
        // ?????? id ?????????????????????????????????????????? id ?????????
        AssertUtils.asserts(id != null, ResultCode.EMP_PHONE_DUPLICATE);
        AssertUtils.asserts(employee.getId().equals(id), ResultCode.EMP_PHONE_DUPLICATE);
    }

    public void checkEmailUnique(Long id, String email) {
        if (StrUtil.isBlank(email)) {
            return;
        }
        Employee employee = employeeMapper.selectOne(Employee::getEmail, email);
        if (employee == null) {
            return;
        }
        // ?????? id ?????????????????????????????????????????? id ?????????
        AssertUtils.asserts(id != null, ResultCode.EMP_EMAIL_DUPLICATE);
        AssertUtils.asserts(employee.getId().equals(id), ResultCode.EMP_EMAIL_DUPLICATE);
    }

}
