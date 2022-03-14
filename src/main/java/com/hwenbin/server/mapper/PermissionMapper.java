package com.hwenbin.server.mapper;

import com.hwenbin.server.core.mapper.MyMapper;
import com.hwenbin.server.entity.Permission;
import com.hwenbin.server.entity.Resource;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @author hwb
 * @create 2022-03-14
 */
public interface PermissionMapper extends MyMapper<Permission> {

    /**
     * 找到所有权限可控资源
     *
     * @return 资源列表
     */
    List<Resource> listResourceWithHandle();

    /**
     * 找到所有权限可控资源
     *
     * @param roleId 角色id
     * @return 资源列表
     */
    List<Resource> listRoleWithResourceByRoleId(@Param("roleId") Long roleId);

    /**
     * 获取所有权限代码
     *
     * @return 代码列表
     */
    @Select("SELECT p.code FROM `permission` p")
    List<String> listAllCode();

}
