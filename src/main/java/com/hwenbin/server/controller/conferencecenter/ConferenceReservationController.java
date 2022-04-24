package com.hwenbin.server.controller.conferencecenter;

import cn.hutool.core.bean.BeanUtil;
import com.hwenbin.server.controller.conferencecenter.req.AddConferenceReservationReq;
import com.hwenbin.server.controller.conferencecenter.req.PageQueryForResListReq;
import com.hwenbin.server.controller.conferencecenter.req.PageQueryForReservationReq;
import com.hwenbin.server.core.web.response.CommonResult;
import com.hwenbin.server.core.web.response.PageResult;
import com.hwenbin.server.core.web.response.ResultGenerator;
import com.hwenbin.server.dto.ConferenceReservationDTO;
import com.hwenbin.server.entity.ConferenceReservationEntity;
import com.hwenbin.server.service.ConferenceReservationService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.security.Principal;
import java.util.Date;

/**
 * @author hwb
 * @date 2022/04/20 22:46
 */
@RestController
@RequestMapping("/conference/reservation")
public class ConferenceReservationController {

    @Resource
    private ConferenceReservationService conferenceReservationService;

    /**
     * 分页查询 —— 会议预订页面
     * @param req 查询参数 + 分页参数
     * @return 分页列表
     */
    @GetMapping
    public CommonResult<PageResult<ConferenceReservationDTO>> pageQueryForReservation(
            @Valid PageQueryForReservationReq req) {
        return ResultGenerator.genOkResult(
                conferenceReservationService.pageQueryForReservation(req)
        );
    }

    /**
     * 新增会议室预订记录
     * @param req 会议预订实体信息
     * @return true
     */
    @PostMapping
    public CommonResult<Boolean> addConferenceReservation(@Valid @RequestBody AddConferenceReservationReq req,
                                                          Principal principal) {
        ConferenceReservationEntity entity = new ConferenceReservationEntity();
        BeanUtil.copyProperties(req, entity);
        entity.setCreator(principal.getName());
        entity.setCreateTime(new Date());
        conferenceReservationService.add(entity);
        return ResultGenerator.genOkResult(true);
    }

    /**
     * 分页查询 —— 会议列表
     * @param req 查询参数 + 分页参数
     * @return 分页列表
     */
    @GetMapping("/resHistory")
    public CommonResult<PageResult<ConferenceReservationEntity>> pageQueryForResHistory(@Valid PageQueryForResListReq req) {
        return ResultGenerator.genOkResult(conferenceReservationService.pageQueryForResHistory(req));
    }

    /**
     * 取消会议 —— 会议列表
     * @param id 会议实体id
     * @return true
     */
    @DeleteMapping("/{id}")
    public CommonResult<Boolean> cancelConferenceRes(@PathVariable Long id) {
        conferenceReservationService.delete(id);
        return ResultGenerator.genOkResult(true);
    }

}
