package com.hwenbin.server.service.impl;

import com.hwenbin.server.dto.AccountWithRole;
import com.hwenbin.server.dto.AccountWithRolePermission;
import com.hwenbin.server.dto.CustomerUserDetails;
import com.hwenbin.server.mapper.PermissionMapper;
import com.hwenbin.server.service.AccountService;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author hwb
 * @create 2022-03-14
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class AccountDetailsServiceImpl implements UserDetailsService {

    @Resource
    private AccountService accountService;

    @Resource
    private PermissionMapper permissionMapper;

    @Override
    public UserDetails loadUserByUsername(final String name) {
        // final AccountWithRolePermission accountWithRolePermission =
        //         this.accountService.findDetailByName(name);
        // // 权限
        // final List<SimpleGrantedAuthority> authorities =
        //         accountWithRolePermission.getPermissionCodeList().stream()
        //                 .map(SimpleGrantedAuthority::new)
        //                 .collect(Collectors.toList());
        // // 角色
        // authorities.add(new SimpleGrantedAuthority(accountWithRolePermission.getRoleName()));
        // return new org.springframework.security.core.userdetails.User(
        //         accountWithRolePermission.getNickname(), "", authorities);
        final AccountWithRolePermission accountWithRolePermission =
                // this.accountService.findDetailByName(name);
                this.accountService.findDetailBy("nickname", name);
        // 权限
        final List<SimpleGrantedAuthority> authorities =
                accountWithRolePermission.getPermissionCodeList().stream()
                        .map(SimpleGrantedAuthority::new)
                        .collect(Collectors.toList());
        Set<String> roleNameSet =
                accountWithRolePermission.getRoles()
                        .stream().map(AccountWithRole.SimpleRole::getRoleName)
                        .collect(Collectors.toCollection(HashSet::new));
        if (roleNameSet.contains("超级管理员")) {
            authorities.clear();
            authorities.addAll(
                    permissionMapper.listAllCode().stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList())
            );
        }
        // 角色
        authorities.addAll(
                roleNameSet.stream()
                        .map(SimpleGrantedAuthority::new)
                        .collect(Collectors.toSet())
        );
        return new CustomerUserDetails(accountWithRolePermission, authorities);
    }

}
