package com.hwenbin.server.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hwenbin.server.controller.managecenter.req.GetRolePageReq;
import com.hwenbin.server.core.web.response.PageResult;
import com.hwenbin.server.dto.RoleDTO;
import com.hwenbin.server.entity.Role;

import java.util.List;

/**
 * @author hwb
 * @create 2022-03-14
 */
public interface RoleService extends IService<Role> {

    /**
     * 获取角色列表，分页+搜索
     * @param req 分页参数 + 筛选条件
     * @return 分页结果
     */
    PageResult<Role> pageQuery(GetRolePageReq req);

    /**
     * 通过id获取角色
     * @param id 角色id
     * @return 角色信息
     */
    Role getRoleById(Long id);

    /**
     * 获取所有状态为 enable 的角色，用于下拉列表
     * @return 状态为 enable 的角色列表
     */
    List<Role> getEnableRoleList();

    /**
     * 新增角色
     * @param roleDTO 新增的角色信息
     */
    void addRole(RoleDTO roleDTO);

    /**
     * 更新角色信息
     * @param roleDTO 更新的角色信息
     */
    void updateRole(RoleDTO roleDTO);

    /**
     * 删除角色，并且删除关联表 [用户-角色、角色-权限] 的相关数据
     * @param roleId 角色id
     */
    void deleteRoleAndCascadeDataById(Long roleId);

    /**
     * 检查角色是否存在，不存在则抛出异常
     * @param roleId 角色id
     * @param deleteOrUpdate 为true时，额外判断是否内置角色，是则抛出异常
     */
    void checkRoleExistsAndBuiltin(Long roleId, Boolean deleteOrUpdate);

    /**
     * 更新角色状态
     * @param roleId 角色id
     * @param status 角色状态
     */
    void updateRoleStatus(Long roleId, Integer status);

}
