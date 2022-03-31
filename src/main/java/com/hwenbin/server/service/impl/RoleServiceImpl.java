package com.hwenbin.server.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hwenbin.server.controller.managecenter.req.GetRolePageReq;
import com.hwenbin.server.core.mybatis.query.MyLambdaQueryWrapper;
import com.hwenbin.server.core.web.response.PageResult;
import com.hwenbin.server.core.web.response.ResultCode;
import com.hwenbin.server.core.web.response.SortingField;
import com.hwenbin.server.dto.RoleDTO;
import com.hwenbin.server.entity.Role;
import com.hwenbin.server.enums.CommonStatusEnum;
import com.hwenbin.server.enums.RoleTypeEnum;
import com.hwenbin.server.mapper.RoleMapper;
import com.hwenbin.server.service.AccountRoleService;
import com.hwenbin.server.service.RolePermissionService;
import com.hwenbin.server.service.RoleService;
import com.hwenbin.server.util.AssertUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.List;

/**
 * @author hwb
 * @create 2022-03-14
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements RoleService {

    @Resource
    private RoleMapper roleMapper;

    @Resource
    private AccountRoleService accountRoleService;

    @Resource
    private RolePermissionService rolePermissionService;

    @Override
    public PageResult<Role> pageQuery(GetRolePageReq req) {
        return roleMapper.selectPage(req,
                Collections.singletonList(new SortingField("sort", SortingField.ORDER_ASC)),
                new MyLambdaQueryWrapper<Role>()
                        .likeIfPresent(Role::getName, req.getName())
                        .likeIfPresent(Role::getCode, req.getCode())
                        .eqIfPresent(Role::getStatus, req.getStatus())
                        .betweenIfPresent(Role::getCreateTime, req.getBeginTime(), req.getEndTime())
        );
    }

    @Override
    public Role getRoleById(Long id) {
        checkRoleExistsAndBuiltin(id, false);
        return roleMapper.selectById(id);
    }

    @Override
    public List<Role> getEnableRoleList() {
        return roleMapper.selectList(
                new MyLambdaQueryWrapper<Role>()
                        .select(Role::getId, Role::getName)
                        .eq(Role::getStatus, CommonStatusEnum.ENABLE)
        );
    }

    @Override
    public void addRole(RoleDTO roleDTO) {
        checkAddOrUpdate(null, roleDTO.getName(), roleDTO.getCode());
        final Role role = new Role();
        BeanUtil.copyProperties(roleDTO, role);
        role.setStatus(CommonStatusEnum.ENABLE.getStatus());
        role.setType(RoleTypeEnum.CUSTOM.getType());
        roleMapper.insert(role);
    }

    @Override
    public void updateRole(RoleDTO roleDTO) {
        checkAddOrUpdate(roleDTO.getId(), roleDTO.getName(), roleDTO.getCode());
        final Role role = new Role();
        BeanUtil.copyProperties(roleDTO, role);
        roleMapper.updateById(role);
    }

    @Override
    public void deleteRoleAndCascadeDataById(Long roleId) {
        // 校验是否存在 与 是否允许删除
        checkRoleExistsAndBuiltin(roleId, true);
        // 逻辑删除
        roleMapper.deleteById(roleId);
        // 删除 account-role
        accountRoleService.deleteItemByRoleId(roleId);
        // 删除 role-permission
        rolePermissionService.deleteItemByRoleId(roleId);
    }

    @Override
    public void updateRoleStatus(Long roleId, Integer status) {
        checkRoleExistsAndBuiltin(roleId, true);
        final Role role = new Role();
        role.setId(roleId);
        role.setStatus(status);
        roleMapper.updateById(role);
    }

    private void checkAddOrUpdate(Long id, String name, String code) {
        // 校验自己存在
        checkRoleExistsAndBuiltin(id, true);
        // 校验角色名的唯一性
        checkRoleNameUnique(id, name);
        // 校验角色标识的唯一性
        checkRoleCodeUnique(id, code);
    }

    // 校验角色是否存在，deleteOrUpdate
    @Override
    public void checkRoleExistsAndBuiltin(Long id, Boolean deleteOrUpdate) {
        if (id == null) {
            return;
        }
        Role role = roleMapper.selectById(id);
        AssertUtils.asserts(role != null, ResultCode.ROLE_NOT_FOUND);
        // 删除和更新操作需要判断是否是内置角色，内置不允许删除和更新
        if (deleteOrUpdate) {
            // 上面已判定role!=null，忽略这里的提示
            AssertUtils.asserts(RoleTypeEnum.CUSTOM.getType().equals(role.getType()),
                    ResultCode.ROLE_TYPE_IS_BUILTIN_NOT_SUPPORT_CHANGE);
        }
    }

    private void checkRoleNameUnique(Long id, String name) {
        Role role = roleMapper.selectOne("name", name);
        if (role == null) {
            return;
        }
        AssertUtils.asserts(id != null, ResultCode.ROLE_NAME_DUPLICATE);
        AssertUtils.asserts(role.getId().equals(id), ResultCode.ROLE_NAME_DUPLICATE);
    }

    private void checkRoleCodeUnique(Long id, String code) {
        Role role = roleMapper.selectOne("code", code);
        if (role == null) {
            return;
        }
        AssertUtils.asserts(id != null, ResultCode.ROLE_CODE_DUPLICATE);
        AssertUtils.asserts(role.getId().equals(id), ResultCode.ROLE_CODE_DUPLICATE);
    }

}
