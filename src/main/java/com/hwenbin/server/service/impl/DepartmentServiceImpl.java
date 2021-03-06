package com.hwenbin.server.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.tree.Tree;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import com.hwenbin.server.controller.managecenter.req.GetAllDeptListReq;
import com.hwenbin.server.core.mybatis.query.MyLambdaQueryWrapper;
import com.hwenbin.server.core.web.response.ResultCode;
import com.hwenbin.server.dto.DepartmentDTO;
import com.hwenbin.server.entity.Department;
import com.hwenbin.server.entity.Employee;
import com.hwenbin.server.enums.CommonStatusEnum;
import com.hwenbin.server.mapper.DepartmentMapper;
import com.hwenbin.server.service.DepartmentService;
import com.hwenbin.server.service.EmployeeService;
import com.hwenbin.server.util.AssertUtils;
import com.hwenbin.server.util.TreeBuildUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author hwb
 * @create 2022-03-25
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class DepartmentServiceImpl extends ServiceImpl<DepartmentMapper, Department> implements DepartmentService {

    private static final Long DEPT_ROOT_ID = 0L;

    @Resource
    private DepartmentMapper departmentMapper;

    @Resource
    private EmployeeService employeeService;

    @Override
    public List<Department> getAllDeptList(GetAllDeptListReq req) {
        List<Department> departments = departmentMapper.selectList(
                new MyLambdaQueryWrapper<Department>()
                        .likeIfPresent(Department::getName, req.getName())
                        .eqIfPresent(Department::getStatus, req.getStatus())
        );
        departments.sort(Comparator.comparing(Department::getSort));
        return departments;
    }

    @Override
    public List<Department> getEnableDeptList() {
        List<Department> departments = departmentMapper.selectList("status", CommonStatusEnum.ENABLE.getStatus());
        departments.sort(Comparator.comparing(Department::getSort));
        return departments;
    }

    @Override
    public Map<Long, String> getDeptMapByIds(Collection<Long> ids) {
        if (ids == null || ids.isEmpty()) {
            return Collections.emptyMap();
        }
        return this.listByIds(ids)
                .stream()
                .collect(Collectors.toMap(Department::getId, Department::getName));
    }

    @Override
    public void addDept(DepartmentDTO departmentDTO) {
        // ???????????????
        if (departmentDTO.getParentId() == null) {
            departmentDTO.setParentId(DEPT_ROOT_ID);
        }
        checkAddOrUpdate(null, departmentDTO.getParentId(), departmentDTO.getName());
        Department department = new Department();
        BeanUtil.copyProperties(departmentDTO, department);
        this.save(department);
    }

    @Override
    public void updateDept(DepartmentDTO departmentDTO) {
        checkAddOrUpdate(departmentDTO.getId(), departmentDTO.getParentId(), departmentDTO.getName());
        // ?????????????????????????????????????????????????????????????????????????????????
        if (CommonStatusEnum.DISABLE.getStatus().equals(departmentDTO.getStatus())) {
            AssertUtils.asserts(
                    employeeService.count(
                            new MyLambdaQueryWrapper<Employee>()
                                    .in(Employee::getDeptId, getDeptIdListWithItAndChildId(departmentDTO.getId()))
                    ) == 0L, ResultCode.DEPT_OR_CHILDREN_EXIST_RELATED_EMPLOYEE_NOT_SUPPORT_CLOSE
            );
        }
        Department department = new Department();
        BeanUtil.copyProperties(departmentDTO, department);
        // todo: ??????????????????????????????null??????updateById??????????????????null???????????????????????????
        this.updateById(department);
    }

    @Override
    public void deleteDept(Long id) {
        checkDeptIdExists(id);
        // ???????????????????????????
        AssertUtils.asserts(
                this.count(new MyLambdaQueryWrapper<Department>().eq(Department::getParentId, id)) == 0,
                ResultCode.DEPT_EXITS_CHILDREN);
        // ????????????????????????????????????????????????????????????
        AssertUtils.asserts(
                employeeService.count(
                        new MyLambdaQueryWrapper<Employee>()
                                .in(Employee::getDeptId, getDeptIdListWithItAndChildId(id))
                ) == 0L, ResultCode.DEPT_OR_CHILDREN_EXIST_RELATED_EMPLOYEE_NOT_SUPPORT_DELETE
        );
        this.removeById(id);
    }

    private void checkAddOrUpdate(Long id, Long parentId, String name) {
        // ????????????id????????????
        checkDeptIdExists(id);
        // ??????????????????????????????
        checkParentDeptEnable(id, parentId);
        // ?????????????????????????????????
        checkDeptNameUnique(id, parentId, name);
    }

    private void checkDeptIdExists(Long id) {
        if (id == null) {
            return;
        }
        Department department = departmentMapper.selectById(id);
        AssertUtils.asserts(department != null, ResultCode.DEPT_NOT_FOUND);
    }

    private void checkParentDeptEnable(Long id, Long parentId) {
        if (parentId == null || DEPT_ROOT_ID.equals(parentId)) {
            return;
        }
        // ??????????????????????????????
        AssertUtils.asserts(!parentId.equals(id), ResultCode.DEPT_PARENT_ERROR);
        // ??????????????????
        Department parentDept = departmentMapper.selectById(parentId);
        AssertUtils.asserts(parentDept != null, ResultCode.DEPT_PARENT_NOT_EXITS);
        // ??????????????????
        // ?????????????????????parentDept??????????????????????????????????????????
        AssertUtils.asserts(CommonStatusEnum.ENABLE.getStatus().equals(parentDept.getStatus()),
                ResultCode.DEPT_NOT_ENABLE);
        // ????????????????????????????????????
        List<Department> children = this.list(new MyLambdaQueryWrapper<Department>().eq(Department::getParentId, id));
        AssertUtils.asserts(children.stream().noneMatch(dept -> dept.getId().equals(parentId)),
                ResultCode.DEPT_PARENT_IS_CHILD);
    }

    private void checkDeptNameUnique(Long id, Long parentId, String name) {
        Department department = this.getOne(new MyLambdaQueryWrapper<Department>().allEq(
                ImmutableMap.of(
                        Department::getParentId, parentId,
                        Department::getName, name
                )
        ));
        if (department == null) {
            return;
        }
        AssertUtils.asserts(id != null, ResultCode.DEPT_NAME_DUPLICATE);
        AssertUtils.asserts(department.getId().equals(id), ResultCode.DEPT_NAME_DUPLICATE);
    }

    // TODO ??????????????????????????????????????????????????????????????????
    @Override
    public Set<Long> getDeptIdListWithItAndChildId(Long id) {
        checkDeptIdExists(id);

        List<Department> deptList = this.list(new MyLambdaQueryWrapper<Department>().select(Department::getId, Department::getParentId));
        ImmutableMultimap.Builder<Long, Department> parentBuilder = ImmutableMultimap.builder();
        deptList.forEach(department -> parentBuilder.put(department.getParentId(), department));
        Multimap<Long, Department> parentDept = parentBuilder.build();

        List<Department> result = new ArrayList<>();
        // ?????????????????????
        this.getDeptIdListWithItAndChildId(result, id, Integer.MAX_VALUE, parentDept);
        // Collection<Long> resultC = CollUtil.trans(result, Department::getId);
        Set<Long> resultS = result.stream().map(Department::getId).collect(Collectors.toSet());
        // ????????????
        resultS.add(id);
        return resultS;
    }

    @Override
    public List<Tree<Long>> buildDeptTreeSelect(List<Department> deptList) {
        if (CollUtil.isEmpty(deptList)) {
            return CollUtil.newArrayList();
        }
        return TreeBuildUtils.build(
                deptList,
                (dept, tree) ->
                        tree
                                .setId(dept.getId())
                                .setParentId(dept.getParentId())
                                .setName(dept.getName())
                                .setWeight(dept.getSort())
        );
    }

    /**
     * ?????????????????????????????????????????? result ??????
     *
     * @param result ??????
     * @param parentId ?????????
     * @param recursiveCount ????????????
     * @param parentDeptMap ????????? Map
     */
    private void getDeptIdListWithItAndChildId(List<Department> result, Long parentId, int recursiveCount,
                                             Multimap<Long, Department> parentDeptMap) {
        // ??????????????? 0????????????
        if (recursiveCount == 0) {
            return;
        }
        // ???????????????
        Collection<Department> deptC = parentDeptMap.get(parentId);
        if (CollUtil.isEmpty(deptC)) {
            return;
        }
        result.addAll(deptC);
        // ????????????
        deptC.forEach(dept -> getDeptIdListWithItAndChildId(result, dept.getId(),
                recursiveCount - 1, parentDeptMap));
    }

}
