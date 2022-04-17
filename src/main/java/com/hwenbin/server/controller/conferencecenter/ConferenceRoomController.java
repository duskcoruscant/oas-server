package com.hwenbin.server.controller.conferencecenter;

import cn.hutool.core.bean.BeanUtil;
import com.hwenbin.server.controller.conferencecenter.req.AddConferenceRoomReq;
import com.hwenbin.server.controller.conferencecenter.req.PageQueryForConferenceRoomReq;
import com.hwenbin.server.controller.conferencecenter.req.UpdateConferenceRoomReq;
import com.hwenbin.server.core.web.response.CommonResult;
import com.hwenbin.server.core.web.response.PageResult;
import com.hwenbin.server.core.web.response.ResultGenerator;
import com.hwenbin.server.dto.ConferenceRoomDTO;
import com.hwenbin.server.entity.ConferenceRoomEntity;
import com.hwenbin.server.service.ConferenceRoomService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.List;

/**
 * @author hwb
 * @date 2022/04/13 17:28
 */
@RestController
@RequestMapping("/conference/room")
public class ConferenceRoomController {

    @Resource
    private ConferenceRoomService conferenceRoomService;

    /**
     * 会议室 分页查询
     * @param req 查询参数 + 分页参数
     * @return 会议室实体列表
     */
    @GetMapping
    public CommonResult<PageResult<ConferenceRoomDTO>> pageQueryForConferenceRoom(
            @Valid PageQueryForConferenceRoomReq req) {
        return ResultGenerator.genOkResult(conferenceRoomService.pageQuery(req));
    }

    /**
     * 获取 会议室 实体
     * @param id 实体id
     * @return 会议室实体
     */
    @GetMapping("/{id}")
    public CommonResult<ConferenceRoomDTO> getConferenceRoomById(@PathVariable Long id) {
        return ResultGenerator.genOkResult(conferenceRoomService.getById(id));
    }

    /**
     * 新增 会议室 实体
     * @param req 实体参数
     * @return true
     */
    @PostMapping
    public CommonResult<Boolean> addConferenceRoom(@Valid @RequestBody AddConferenceRoomReq req) {
        ConferenceRoomDTO dto = new ConferenceRoomDTO();
        BeanUtil.copyProperties(req, dto);
        conferenceRoomService.add(dto);
        return ResultGenerator.genOkResult(true);
    }

    /**
     * 更新 会议室 实体
     * @param req 实体参数
     * @return true
     */
    @PutMapping
    public CommonResult<Boolean> updateConferenceRoom(@Valid @RequestBody UpdateConferenceRoomReq req) {
        ConferenceRoomDTO dto = new ConferenceRoomDTO();
        BeanUtil.copyProperties(req, dto);
        conferenceRoomService.update(dto);
        return ResultGenerator.genOkResult(true);
    }

    /**
     * 删除 会议室 实体
     * @param id 实体id
     * @return true
     */
    @DeleteMapping("/{id}")
    public CommonResult<Boolean> deleteConferenceRoomById(@PathVariable Long id) {
        conferenceRoomService.deleteById(id);
        return ResultGenerator.genOkResult(true);
    }

    /**
     * 获取 会议室列表（id + code）
     * @return 会议室实体列表
     */
    @GetMapping("/listAll")
    public CommonResult<List<ConferenceRoomEntity>> getAllConferenceRoomList() {
        return ResultGenerator.genOkResult(conferenceRoomService.listAllConferenceRoom());
    }

}
