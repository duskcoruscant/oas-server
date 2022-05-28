package com.hwenbin.server.controller.managecenter.resp;

import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * @author hwb
 * @date 2022/05/26 21:37
 */
@Data
@Builder
public class ImportExcelEmployeesResp {

    /**
     * 创建成功的员工账户昵称数组
     */
    private List<String> createNicknames;

    /**
     * 更新成功的员工账户昵称数组
     */
    private List<String> updateNicknames;

    /**
     * 导入失败的员工集合：key 为员工账户昵称，value 为失败原因
     */
    private Map<String, String> failureNicknames;

}
