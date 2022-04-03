package com.hwenbin.server.core.web.response;

/**
 * 响应状态码枚举类
 *
 * <p>自定义业务异常 2*** 开始
 *
 * <p>原有类异常 4*** 开始
 *
 * @author hwb
 * @create 2022-03-14
 */
public enum ResultCode {

    SUCCEED_REQUEST_FAILED_RESULT(1000, "成功请求，但结果不是期望的成功结果"),

    FIND_FAILED(2000, "查询失败"),

    SAVE_FAILED(2001, "保存失败"),

    UPDATE_FAILED(2002, "更新失败"),

    DELETE_FAILED(2003, "删除失败"),

    DUPLICATE_NAME(2004, "账户昵称重复"),

    LOGIN_EMPTY_NAME_OR_EMAIL(2005, "账户昵称或邮箱为空"),

    LOGIN_EMPTY_PASSWORD(2006, "密码为空"),

    LOGIN_ERROR_NICKNAME(2007, "账户昵称错误"),

    LOGIN_ERROR_EMAIL(2008, "邮箱错误"),

    LOGIN_ERROR_PASSWORD(2009, "密码错误"),

    LOGIN_RELATED_EMPLOYEE_STATUS_DISABLE(2010, "当前账户绑定员工状态为disable，不允许登录"),

    /*
    员工 + 考勤
     */
    EMP_NOT_FOUND(2100, "当前操作的员工不存在"),

    EMP_TODAY_HAS_CLOCKED_IN(2101, "该员工今日已签到过"),

    EMP_TODAY_HAS_NOT_CLOCKED_IN(2102, "该员工今日还没有签到"),

    EMP_TODAY_HAS_CLOCKED_OUT(2103, "该员工今日已经签退过"),

    /*
    部门
     */
    DEPT_NOT_FOUND(2200, "当前操作的部门不存在"),

    DEPT_PARENT_ERROR(2201, "不能设置自己为父部门"),

    DEPT_PARENT_NOT_EXITS(2202, "父部门不存在"),

    DEPT_NOT_ENABLE(2203, "所选父级部门处于关闭状态，不允许选择"),

    DEPT_PARENT_IS_CHILD(2204, "父部门不能是原来的子部门"),

    DEPT_NAME_DUPLICATE(2205, "当前选择的父级部门下已经存在该名字的部门"),

    DEPT_EXITS_CHILDREN(2206, "存在子部门，无法删除"),

    /*
    职位
     */
    POSITION_NOT_FOUND(2300, "当前操作的职位不存在"),

    POSITION_NAME_DUPLICATE(2301, "已经存在该名字的职位"),

    POSITION_CODE_DUPLICATE(2302, "已经存在该标识的职位"),

    /**
     * 角色
     */
    ROLE_NOT_FOUND(2400, "当前操作的角色不存在"),

    ROLE_NAME_DUPLICATE(2401, "已经存在该名字的角色"),

    ROLE_CODE_DUPLICATE(2402, "已经存在该标识的角色"),

    ROLE_TYPE_IS_BUILTIN_NOT_SUPPORT_CHANGE(2402, "当前操作的角色为系统内置角色，不允许变更"),

    /*
     * 原有类异常
     */

    DATABASE_EXCEPTION(4001, "数据库异常"),

    UNAUTHORIZED_EXCEPTION(4002, "认证异常"),

    VIOLATION_EXCEPTION(4003, "验证异常");

    private final int value;

    private final String reason;

    ResultCode(final int value, final String reason) {
        this.value = value;
        this.reason = reason;
    }

    public int getValue() {
        return this.value;
    }

    public String getReason() {
        return this.reason;
    }

    public String format(final Object... objects) {
        return objects.length > 0 ? String.format(this.getReason(), objects) : this.getReason();
    }

}
