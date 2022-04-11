package com.hwenbin.server.controller.worklogcenter;

import com.hwenbin.server.controller.worklogcenter.req.AddWorkLogReq;
import com.hwenbin.server.controller.worklogcenter.req.PageQueryForWorkLogReq;
import com.hwenbin.server.controller.worklogcenter.req.UpdateWorkLogReq;
import com.hwenbin.server.controller.worklogcenter.req.PageQueryForReceiveLogReq;
import com.hwenbin.server.core.web.response.CommonResult;
import com.hwenbin.server.core.web.response.PageResult;
import com.hwenbin.server.core.web.response.ResultGenerator;
import com.hwenbin.server.dto.WorkLogSendDTO;
import com.hwenbin.server.entity.WorkLogEntity;
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
     * 日志 分页查询
     * @param req 日志参数 + 分页参数
     * @return 分页结果
     */
    @GetMapping
    public CommonResult<PageResult<WorkLogEntity>> pageQueryForWorkLog(@Valid PageQueryForWorkLogReq req) {
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
     * 查询日志详情
     * @param id 日志id
     * @return 日志实体
     */
    @GetMapping("{id}")
    public CommonResult<WorkLogEntity> getWorkLogById(@PathVariable Long id) {
        return ResultGenerator.genOkResult(workLogService.getById(id));
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

}
