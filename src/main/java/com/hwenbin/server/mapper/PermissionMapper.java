package com.hwenbin.server.mapper;

import com.hwenbin.server.core.mybatis.mapper.MyBaseMapper;
import com.hwenbin.server.entity.Permission;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @author hwb
 * @create 2022-03-14
 */
public interface PermissionMapper extends MyBaseMapper<Permission> {

    /**
     * 获取所有权限代码
     *
     * @return 权限标识列表
     */
    @Select("SELECT p.code FROM `permission` p")
    List<String> listAllCode();

}
