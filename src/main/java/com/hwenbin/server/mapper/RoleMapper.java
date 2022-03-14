package com.hwenbin.server.mapper;

import com.hwenbin.server.core.mapper.MyMapper;
import com.hwenbin.server.dto.RoleWithResource;
import com.hwenbin.server.entity.Role;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author hwb
 * @create 2022-03-14
 */
public interface RoleMapper extends MyMapper<Role> {

    /**
     * 获取所有角色以及对应的权限
     *
     * @return 角色可控资源列表
     */
    List<RoleWithResource> listRoles();

    /**
     * 按角色Id更新修改时间
     *
     * @param id 角色Id
     */
    void updateTimeById(@Param("id") Long id);

}
