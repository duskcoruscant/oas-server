package com.hwenbin.server.service.impl;

import com.hwenbin.server.mapper.DepartmentMapper;
import com.hwenbin.server.entity.Department;
import com.hwenbin.server.service.DepartmentService;
import com.hwenbin.server.core.service.AbstractService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
* @author hwb
* @create 
*/
@Service
@Transactional(rollbackFor = Exception.class)
public class DepartmentServiceImpl extends AbstractService<Department> implements DepartmentService {
@Resource
private DepartmentMapper departmentMapper;

}
