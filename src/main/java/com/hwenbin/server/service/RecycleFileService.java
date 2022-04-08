package com.hwenbin.server.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hwenbin.server.core.web.response.PageParam;
import com.hwenbin.server.core.web.response.PageResult;
import com.hwenbin.server.dto.RecycleFileDTO;
import com.hwenbin.server.entity.RecycleFile;

/**
 * @author hwb
 * @date 2022/04/07 18:17
 */
public interface RecycleFileService extends IService<RecycleFile> {

    /**
     * 分页查询
     * @param empId 员工id
     * @param pageParam 分页参数
     * @return 分页结果
     */
    PageResult<RecycleFileDTO> pageQuery(Long empId, PageParam pageParam);

    /**
     * 创建记录
     * @param fileId 文件id
     * @param empId 创建人
     * @param deletedBatchNum 删除批次号
     */
    void addRecycleFile(Long fileId, Long empId, String deletedBatchNum);

}
