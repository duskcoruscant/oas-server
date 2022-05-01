package com.hwenbin.server.dto;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import java.sql.Timestamp;

/**
 * @author hwb
 * @create 2022-03-14
 */
@Data
public class AccountDTO {

    /**
     * 员工Id
     */
    private Long id;

    /**
     * 邮箱
     */
    @Email
    private String email;

    /**
     * 用户名
     */
    @NotEmpty(message = "用户名不能为空")
    private String nickname;

    /**
     * 密码
     */
    @NotEmpty(message = "密码不能为空")
    private String password;

    /**
     * 注册时间
     */
    private Timestamp registerTime;

    /**
     * 上一次登录时间
     */
    private Timestamp loginTime;

    private Long roleId;

    private Long deptId;

    private String empName;

}
