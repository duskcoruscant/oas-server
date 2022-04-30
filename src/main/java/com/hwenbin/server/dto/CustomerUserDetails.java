package com.hwenbin.server.dto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

/**
 * org.springframework.security.core.userdetails.UserDetails这个接口表示用户信息，SpringSecurity默认实现了一个User，
 * 不过字段寥寥无几，只有username，password这些，而且后面获取用户信息的时候也是获取的UserDetail。
 *
 * 于是我们自己实现这个接口，对User进行扩充字段，实现基于数据库的用户角色配置认证
 *
 * @author hwb
 * @date 2022/04/30 13:38
 */
public class CustomerUserDetails implements UserDetails {

    @Getter
    @Setter
    private Long id;

    @Setter
    private String userName;

    @Setter
    private String password;

    @Getter
    @Setter
    private Collection<AccountWithRole.SimpleRole> roles;

    @Setter
    private Collection<? extends GrantedAuthority> authorities;

    public CustomerUserDetails(AccountWithRolePermission user, Collection<? extends GrantedAuthority> authorities){
        this.setId(user.getId());
        this.setUserName(user.getNickname());
        this.setPassword(user.getPassword());
        this.setRoles(user.getRoles());
        this.setAuthorities(authorities);
    }

    /**
     * 获取用户拥有的权限和角色
     * @return
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.authorities;
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.userName;
    }

    /**
     * 账户是否未过期
     * @return
     */
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    /**
     * 是否未禁用
     * @return
     */
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    /**
     * 密码是否未过期
     * @return
     */
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    /**
     * 是否启用
     * @return
     */
    @Override
    public boolean isEnabled() {
        return true;
    }

}
