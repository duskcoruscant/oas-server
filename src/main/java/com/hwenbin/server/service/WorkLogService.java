package com.hwenbin.server.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hwenbin.server.controller.worklogcenter.req.AddWorkLogReq;
import com.hwenbin.server.controller.worklogcenter.req.PageQueryForReceiveLogReq;
import com.hwenbin.server.controller.worklogcenter.req.PageQueryForWorkLogReq;
import com.hwenbin.server.controller.worklogcenter.req.UpdateWorkLogReq;
import com.hwenbin.server.core.web.response.PageResult;
import com.hwenbin.server.dto.WorkLogDTO;
import com.hwenbin.server.dto.WorkLogSendDTO;
import com.hwenbin.server.entity.WorkLogEntity;
import com.hwenbin.server.entity.WorkLogSendEntity;

/**
 * @author hwb
 * @date 2022/04/10 21:17
 */
public interface WorkLogService extends IService<WorkLogEntity> {

    /**
     * 分页查询
     * @param req 日志参数 + 分页参数
     * @return 结果
     */
    PageResult<WorkLogDTO> pageQuery(PageQueryForWorkLogReq req);

    /**
     * 新增日志
     * @param req 日志参数
     */
    void add(AddWorkLogReq req);

    /**
     * 更新日志
     * @param req 日志参数
     */
    void updateById(UpdateWorkLogReq req);

    /**
     * 我收到的日志 分页查询
     * @param req 日志参数 + 分页参数
     * @return 结果
     */
    PageResult<WorkLogSendDTO> pageQueryForReceiveLog(PageQueryForReceiveLogReq req);

    /**
     * 更新workLogSend实体
     * @param workLogSendEntity 实体
     */
    void updateWorkLogSendById(WorkLogSendEntity workLogSendEntity);

    /**
     * 获取WorkLogDTO，其内包含该日志详情及评论列表
     * @param id 日志实体id
     * @return workLogDTO（与实体相比多了comments）
     */
    WorkLogDTO getWorkLogWithCommentsById(Long id);

    /**
     * 获取日志详情及其 通知人ids
     * @param id 日志实体id
     * @return workLogDTO（与实体相比多了sendEmpIds）
     */
    WorkLogDTO getWorkLogWithSendEmpIdsById(Long id);

}
