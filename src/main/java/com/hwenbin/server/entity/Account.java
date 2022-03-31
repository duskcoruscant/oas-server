package com.hwenbin.server.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.sql.Timestamp;

/**
 * @author hwb
 * @create 2022-03-14
 */
@TableName("account")
@Data
public class Account {

    /**
     * 员工Id
     */
    @TableId
    private Long id;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 用户名
     */
    private String nickname;

    /**
     * 密码
     */
    private String password;

    /**
     * 注册时间
     */
    private Timestamp registerTime;

    /**
     * 上一次登录时间
     */
    private Timestamp loginTime;

}
