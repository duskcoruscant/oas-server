package com.hwenbin.server.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hwenbin.server.dto.PermissionDTO;
import com.hwenbin.server.entity.Permission;
import com.hwenbin.server.mapper.PermissionMapper;
import com.hwenbin.server.service.PermissionService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author hwb
 * @create 2022-03-14
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class PermissionServiceImpl extends ServiceImpl<PermissionMapper, Permission>
        implements PermissionService {

    @Resource
    private PermissionMapper permissionMapper;

    @Override
    public List<PermissionDTO> getAllPermissionList() {
        return permissionMapper.selectList().stream().map(permission -> {
            PermissionDTO dto = new PermissionDTO();
            BeanUtil.copyProperties(permission, dto);
            return dto;
        }).collect(Collectors.toList());
    }

}
