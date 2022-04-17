package com.hwenbin.server.controller.conferencecenter;

import cn.hutool.core.bean.BeanUtil;
import com.hwenbin.server.controller.conferencecenter.req.AddConferenceEquipmentReq;
import com.hwenbin.server.controller.conferencecenter.req.PageQueryForConferenceEquipmentReq;
import com.hwenbin.server.controller.conferencecenter.req.UpdateConferenceEquipmentReq;
import com.hwenbin.server.core.web.response.CommonResult;
import com.hwenbin.server.core.web.response.PageResult;
import com.hwenbin.server.core.web.response.ResultGenerator;
import com.hwenbin.server.dto.ConferenceEquipmentDTO;
import com.hwenbin.server.dto.ConferenceEquipmentTreeNode;
import com.hwenbin.server.entity.ConferenceEquipmentEntity;
import com.hwenbin.server.service.ConferenceEquipmentService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Nullable;
import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.List;

/**
 * @author hwb
 * @date 2022/04/13 16:56
 */
@RestController
@RequestMapping("/conference/equipment")
public class ConferenceEquipmentController {

    @Resource
    private ConferenceEquipmentService conferenceEquipmentService;

    /**
     * 会议室设备 分页查询
     * @param req 查询参数 + 分页参数
     * @return 会议室设备实体列表
     */
    @GetMapping
    public CommonResult<PageResult<ConferenceEquipmentEntity>> pageQueryForConferenceEquipment(
            @Valid PageQueryForConferenceEquipmentReq req) {
        return ResultGenerator.genOkResult(conferenceEquipmentService.pageQuery(req));
    }

    /**
     * 获取 会议室设备 实体
     * @param id 实体id
     * @return 会议室设备实体
     */
    @GetMapping("/{id}")
    public CommonResult<ConferenceEquipmentDTO> getConferenceEquipmentById(@PathVariable Long id) {
        return ResultGenerator.genOkResult(conferenceEquipmentService.getById(id));
    }

    /**
     * 新增 会议室设备 实体
     * @param req 实体参数
     * @return true
     */
    @PostMapping
    public CommonResult<Boolean> addConferenceEquipment(@Valid @RequestBody AddConferenceEquipmentReq req) {
        ConferenceEquipmentDTO dto = new ConferenceEquipmentDTO();
        BeanUtil.copyProperties(req, dto);
        conferenceEquipmentService.add(dto);
        return ResultGenerator.genOkResult(true);
    }

    /**
     * 更新 会议室设备 实体
     * @param req 实体参数
     * @return true
     */
    @PutMapping
    public CommonResult<Boolean> updateConferenceEquipment(@Valid @RequestBody UpdateConferenceEquipmentReq req) {
        ConferenceEquipmentDTO dto = new ConferenceEquipmentDTO();
        BeanUtil.copyProperties(req, dto);
        conferenceEquipmentService.update(dto);
        return ResultGenerator.genOkResult(true);
    }

    /**
     * 删除 会议室 实体
     * @param id 实体id
     * @return true
     */
    @DeleteMapping("/{id}")
    public CommonResult<Boolean> deleteConferenceEquipmentById(@PathVariable Long id) {
        conferenceEquipmentService.deleteById(id);
        return ResultGenerator.genOkResult(true);
    }

    /**
     * 获取 按类型分类的树形结构数据列表，用于创建 / 更改 会议室时的设备下拉选项
     * @param conferenceRoomId 更新表单需要传会议室id
     * @return 数据列表
     */
    @GetMapping("/groupByTypeList")
    public CommonResult<List<ConferenceEquipmentTreeNode>> getConferenceEquipmentListGroupingByType(
            @Nullable @RequestParam Long conferenceRoomId
    ) {
        return ResultGenerator.genOkResult(conferenceEquipmentService.groupByTypeList(conferenceRoomId));
    }

    /**
     * 获取 已存在的设备类型列表（去重）
     * @return 设备类型列表
     */
    @GetMapping("/listAllTypes")
    public CommonResult<List<String>> getAllEquipmentTypeList() {
        return ResultGenerator.genOkResult(conferenceEquipmentService.listAllTypes());
    }

}
