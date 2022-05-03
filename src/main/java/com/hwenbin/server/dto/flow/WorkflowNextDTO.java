package com.hwenbin.server.dto.flow;

import com.hwenbin.server.entity.Employee;
import com.hwenbin.server.entity.Role;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 动态人员、组
 *
 * @author hwb
 * @date 2022/05/01 16:08
 */
@Data
public class WorkflowNextDTO implements Serializable {

    private String type;

    private String vars;

    private List<Employee> userList;

    private List<Role> roleList;

}
