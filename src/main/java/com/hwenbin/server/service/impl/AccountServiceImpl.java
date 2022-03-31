package com.hwenbin.server.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hwenbin.server.core.exception.ServiceException;
import com.hwenbin.server.core.jwt.JwtUtil;
import com.hwenbin.server.core.mybatis.query.MyLambdaQueryWrapper;
import com.hwenbin.server.core.web.response.ResultCode;
import com.hwenbin.server.dto.AccountDTO;
import com.hwenbin.server.dto.AccountWithRolePermission;
import com.hwenbin.server.entity.Account;
import com.hwenbin.server.entity.AccountRole;
import com.hwenbin.server.entity.Employee;
import com.hwenbin.server.enums.CommonStatusEnum;
import com.hwenbin.server.mapper.AccountMapper;
import com.hwenbin.server.mapper.AccountRoleMapper;
import com.hwenbin.server.mapper.PermissionMapper;
import com.hwenbin.server.service.AccountService;
import com.hwenbin.server.service.EmployeeService;
import com.hwenbin.server.util.AssertUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

/**
 * @author hwb
 * @create 2022-03-14
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class AccountServiceImpl extends ServiceImpl<AccountMapper, Account> implements AccountService {

    @Resource
    private AccountMapper accountMapper;

    @Resource
    private AccountRoleMapper accountRoleMapper;

    @Resource
    private PermissionMapper permissionMapper;

    @Resource
    private PasswordEncoder passwordEncoder;

    @Resource
    private AccountDetailsServiceImpl userDetailsService;

    @Resource
    private EmployeeService employeeService;

    @Resource
    private JwtUtil jwtUtil;

    // 普通用户角色Id
    private static final Long defaultRoleId = 2L;

    @Override
    public String getToken(String name) {
        final UserDetails accountDetails = this.userDetailsService.loadUserByUsername(name);
        return this.jwtUtil.sign(name, accountDetails.getAuthorities());
    }

    @Override
    public void register(AccountDTO accountDto) {
        Account a = this.getOne(new MyLambdaQueryWrapper<Account>().eq(Account::getNickname, accountDto.getNickname()));
        if (a != null) {
            throw new ServiceException("用户名已存在");
        } else {
            a = this.getOne(new MyLambdaQueryWrapper<Account>().eq(Account::getEmail, accountDto.getEmail()));
            if (a != null) {
                throw new ServiceException("邮箱已存在");
            } else {
                // log.info("before password : {}", account.getPassword().trim());
                accountDto.setPassword(this.passwordEncoder.encode(accountDto.getPassword().trim()));
                // log.info("after password : {}", account.getPassword());
                final Account account = new Account();
                BeanUtil.copyProperties(accountDto, account);
                this.accountMapper.insert(account);
                // log.info("Account<{}> id : {}", account.getName(), account.getId());
                this.saveRole(account.getId(), accountDto.getRoleId());
            }
        }
    }

    @Override
    public String login(Account account) {
        // {"name":"admin", "password":"admin123"}
        // {"email":"admin@qq.com", "password":"admin123"}
        // 用户名或邮箱为空
        AssertUtils.asserts(account.getNickname() != null || account.getEmail() != null,
                ResultCode.LOGIN_EMPTY_NAME_OR_EMAIL);
        // 密码为空
        AssertUtils.asserts(account.getPassword() != null, ResultCode.LOGIN_EMPTY_PASSWORD);
        // 用户名登录
        Account dbAccount = null;
        if (account.getNickname() != null) {
            dbAccount = accountMapper.selectOne("nickname", account.getNickname());
            AssertUtils.asserts(dbAccount != null, ResultCode.LOGIN_ERROR_NICKNAME);
        }
        // 邮箱登录
        if (account.getEmail() != null) {
            dbAccount = accountMapper.selectOne("email", account.getEmail());
            AssertUtils.asserts(dbAccount != null, ResultCode.LOGIN_ERROR_EMAIL);
            // 已经判断dbAccount不为null，忽略提示
            account.setNickname(dbAccount.getNickname());
        }
        // 验证密码   // 已经判断dbAccount不为null，忽略提示
        AssertUtils.asserts(verifyPassword(account.getPassword(), dbAccount.getPassword()),
                ResultCode.LOGIN_ERROR_PASSWORD);
        // 若此时员工状态为禁用，则不允许登录
        AssertUtils.asserts(
                CommonStatusEnum.ENABLE.getStatus().equals(
                        employeeService.getEmployeeById(dbAccount.getId()).getStatus()),
                ResultCode.LOGIN_RELATED_EMPLOYEE_STATUS_DISABLE
        );
        // 更新登录时间
        updateLoginTimeByName(account.getNickname());
        return getToken(account.getNickname());
    }

    private void saveRole(final Long accountId, Long roleId) {
        // 如果没有指定角色Id，以默认普通用户roleId保存
        if (roleId == null) {
            roleId = defaultRoleId;
        }
        final AccountRole accountRole = new AccountRole();
        accountRole.setAccountId(accountId);
        accountRole.setRoleId(roleId);
        this.accountRoleMapper.insert(accountRole);
    }

    @Override
    public String updateMe(Account account) {
        // 如果修改了密码
        if (account.getPassword() != null && account.getPassword().length() >= 6) {
            // 密码修改后需要加密
            account.setPassword(this.passwordEncoder.encode(account.getPassword().trim()));
        }
        this.accountMapper.updateById(account);
        // 还要更新员工表的信息
        final Employee employee = new Employee();
        employee.setId(account.getId());
        employee.setEmail(account.getEmail());
        employee.setNickname(account.getNickname());
        employeeService.updateById(employee);
        final Account dbAccount = accountMapper.selectById(account.getId());
        return getToken(dbAccount.getNickname());
    }

    @Override
    public AccountWithRolePermission findDetailBy(final String column, final Object params) {
        final Map<String, Object> map = new HashMap<>(1);
        map.put(column, params);
        return this.accountMapper.findDetailBy(map);
    }

    @Override
    public AccountWithRolePermission findDetailByName(final String name)
            throws UsernameNotFoundException {
        final AccountWithRolePermission account = this.findDetailBy("nickname", name);
        if (account == null) {
            throw new UsernameNotFoundException("没有找到该账户昵称");
        }
        if ("超级管理员".equals(account.getRoleName())) {
            // 超级管理员所有权限都有
            account.setPermissionCodeList(this.permissionMapper.listAllCode());
        }
        return account;
    }

    @Override
    public Boolean verifyPassword(final String rawPassword, final String encodedPassword) {
        return this.passwordEncoder.matches(rawPassword, encodedPassword);
    }

    @Override
    public void updateLoginTimeByName(final String nickname) {
        this.accountMapper.updateLoginTimeByName(nickname);
    }

}
