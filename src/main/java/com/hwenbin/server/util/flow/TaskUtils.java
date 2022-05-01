package com.hwenbin.server.util.flow;

import cn.hutool.core.util.ObjectUtil;
import com.hwenbin.server.core.constant.TaskConstant;
import com.hwenbin.server.dto.CustomerUserDetails;
import com.hwenbin.server.util.ContextUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * 工作流任务工具类
 *
 * @author hwb
 * @date 2022/04/30 01:13
 */
public class TaskUtils {

    public static String getUserId() {
        return String.valueOf(Objects.requireNonNull(ContextUtils.getCustomerUserDetails()).getId());
    }

    /**
     * 获取用户组信息
     *
     * @return candidateGroup
     */
    public static List<String> getCandidateGroup() {
        List<String> list = new ArrayList<>();
        CustomerUserDetails user = ContextUtils.getCustomerUserDetails();
        if (ObjectUtil.isNotNull(user)) {
            if (ObjectUtil.isNotEmpty(user.getRoles())) {
                user.getRoles().forEach(role -> list.add(TaskConstant.ROLE_GROUP_PREFIX + role.getRoleId()));
            }
            if (ObjectUtil.isNotNull(user.getDeptId())) {
                list.add(TaskConstant.DEPT_GROUP_PREFIX + user.getDeptId());
            }
        }
        return list;
    }

}
