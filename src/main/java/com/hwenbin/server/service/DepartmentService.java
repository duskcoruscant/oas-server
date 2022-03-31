package com.hwenbin.server.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hwenbin.server.controller.managecenter.req.GetAllDeptListReq;
import com.hwenbin.server.dto.DepartmentDTO;
import com.hwenbin.server.entity.Department;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author hwb
 * @create 2022-03-25
 */
public interface DepartmentService extends IService<Department> {

    /**
     * 获取所有部门
     * @param req 筛选条件
     * @return 部门列表
     */
    List<Department> getAllDeptList(GetAllDeptListReq req);

    /**
     * 获取 所有状态为 enable(0) 的部门
     * @return 部门列表
     */
    List<Department> getEnableDeptList();

    /**
     * 通过id集合，获取对应部门名称map
     * @param ids 部门id列表
     * @return map：key = 部门id，value = 部门名称
     */
    Map<Long, String> getDeptMapByIds(Collection<Long> ids);

    /**
     * 添加部门
     * @param departmentDTO 新增的部门信息
     */
    void addDept(DepartmentDTO departmentDTO);

    /**
     * 更新部门
     * @param departmentDTO 更新的部门信息
     */
    void updateDept(DepartmentDTO departmentDTO);

    /**
     * 删除部门
     * @param id 部门id
     */
    void deleteDept(Long id);

    /**
     * 获取 指定deptId及其子部门(所有)的dept 的集合
     * @param id 部门id
     * @return 部门id列表
     */
    Set<Long> getDeptIdListWithItAndChildId(Long id);

}
