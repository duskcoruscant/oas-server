package com.hwenbin.server.dto;

import com.hwenbin.server.entity.Resource;
import com.hwenbin.server.entity.Role;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Transient;
import java.util.List;

/**
 * @author hwb
 * @create 2022-03-14
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class RoleWithResource extends Role {

    /** 角色对应的权限 */
    @Transient
    private List<Resource> resourceList;

}
