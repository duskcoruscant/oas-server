package com.hwenbin.server.controller.worklogcenter;

import cn.hutool.core.bean.BeanUtil;
import com.hwenbin.server.controller.worklogcenter.req.*;
import com.hwenbin.server.core.web.response.CommonResult;
import com.hwenbin.server.core.web.response.PageResult;
import com.hwenbin.server.core.web.response.ResultGenerator;
import com.hwenbin.server.dto.WorkLogDTO;
import com.hwenbin.server.dto.WorkLogSendDTO;
import com.hwenbin.server.entity.WorkLogSendEntity;
import com.hwenbin.server.service.WorkLogService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;

/**
 * 日志控制器
 *
 * @author hwb
 * @date 2022/04/10 21:19
 */
@RestController
@RequestMapping("/workLog")
public class WorkLogController {

    @Resource
    private WorkLogService workLogService;

    /**
     * 日志 分页查询 —— 我发出的
     * @param req 日志参数 + 分页参数
     * @return 分页结果
     */
    @GetMapping
    public CommonResult<PageResult<WorkLogDTO>> pageQueryForWorkLog(@Valid PageQueryForWorkLogReq req) {
        return ResultGenerator.genOkResult(workLogService.pageQuery(req));
    }

    /**
     * 新增日志
     * @param req 日志参数
     * @return true
     */
    @PostMapping
    public CommonResult<Boolean> addWorkLog(@Valid @RequestBody AddWorkLogReq req) {
        workLogService.add(req);
        return ResultGenerator.genOkResult(true);
    }

    /**
     * 更新日志
     * @param req 日志参数
     * @return true
     */
    @PutMapping
    public CommonResult<Boolean> updateWorkLog(@Valid @RequestBody UpdateWorkLogReq req) {
        workLogService.updateById(req);
        return ResultGenerator.genOkResult(true);
    }

    /**
     * 查询日志详情及其 通知人ids
     * @param id 日志id
     * @return workLogDTO（与实体相比多了sendEmpIds）
     */
    @GetMapping("{id}")
    public CommonResult<WorkLogDTO> getWorkLogById(@PathVariable Long id) {
        return ResultGenerator.genOkResult(workLogService.getWorkLogWithSendEmpIdsById(id));
    }

    /**
     * 获取日志详情及评论列表
     * @param id 日志id
     * @return workLogDTO（与实体相比多了comments）
     */
    @GetMapping("/workLogWithComments/{id}")
    public CommonResult<WorkLogDTO> getWorkLogWithCommentsById(@PathVariable Long id) {
        return ResultGenerator.genOkResult(workLogService.getWorkLogWithCommentsById(id));
    }

    /**
     * 我收到的日志 分页查询
     * @param req 日志参数 + 分页参数
     * @return 日志
     */
    @GetMapping("/receive")
    public CommonResult<PageResult<WorkLogSendDTO>> pageQueryForReceiveLog(@Valid PageQueryForReceiveLogReq req) {
        return ResultGenerator.genOkResult(workLogService.pageQueryForReceiveLog(req));
    }

    /**
     * 已读或评论收到的日志
     * @param req id + 已读标识 + 评论
     * @return true
     */
    @PutMapping("/receive")
    public CommonResult<Boolean> readOrCommentReceiveLog(@Valid @RequestBody ReadOrCommentReceiveLogReq req) {
        WorkLogSendEntity workLogSendEntity = new WorkLogSendEntity();
        BeanUtil.copyProperties(req, workLogSendEntity);
        workLogService.updateWorkLogSendById(workLogSendEntity);
        return ResultGenerator.genOkResult(true);
    }

}
