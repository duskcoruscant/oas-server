package com.hwenbin.server.controller;

import com.hwenbin.server.core.response.Result;
import com.hwenbin.server.core.response.ResultGenerator;
import com.hwenbin.server.entity.Department;
import com.hwenbin.server.service.DepartmentService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
* @author hwb
* @create 2022-03-14
*/
@RestController
@RequestMapping("/department")
public class DepartmentController {

@Resource
private DepartmentService departmentService;

@PostMapping
public Result add(@RequestBody Department department) {
departmentService.save(department);
return ResultGenerator.genOkResult();
}

@DeleteMapping("/{id}")
public Result delete(@PathVariable Long id) {
departmentService.deleteById(id);
return ResultGenerator.genOkResult();
}

@PatchMapping
public Result update(@RequestBody Department department) {
departmentService.update(department);
return ResultGenerator.genOkResult();
}

@GetMapping("/{id}")
public Result detail(@PathVariable Long id) {
Department department = departmentService.getById(id);
return ResultGenerator.genOkResult(department);
}

@GetMapping
public Result list(@RequestParam(defaultValue = "0") Integer page,
@RequestParam(defaultValue = "0") Integer size) {
PageHelper.startPage(page, size);
List<Department> list = departmentService.listAll();
PageInfo<Department> pageInfo = PageInfo.of(list);
return ResultGenerator.genOkResult(pageInfo);
}

}
