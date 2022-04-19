package com.hwenbin.server.controller.missivecenter;

import cn.hutool.core.bean.BeanUtil;
import com.hwenbin.server.controller.missivecenter.req.PageQueryForMissiveReq;
import com.hwenbin.server.controller.missivecenter.req.UpdateMissiveReq;
import com.hwenbin.server.core.web.response.CommonResult;
import com.hwenbin.server.core.web.response.PageResult;
import com.hwenbin.server.core.web.response.ResultGenerator;
import com.hwenbin.server.dto.MissiveDTO;
import com.hwenbin.server.entity.MissiveEntity;
import com.hwenbin.server.service.MissiveService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;

/**
 * @author hwb
 * @date 2022/04/17 23:47
 */
@RestController
@RequestMapping("/missive")
public class MissiveController {

    @Resource
    private MissiveService missiveService;

    /**
     * 公文 分页查询
     * @param req 查询参数 + 分页参数
     * @return 公文实体列表
     */
    @GetMapping
    public CommonResult<PageResult<MissiveDTO>> pageQueryForMissive(@Valid PageQueryForMissiveReq req) {
        return ResultGenerator.genOkResult(missiveService.pageQuery(req));
    }

    /**
     * 获取 公文 实体
     * @param id 实体id
     * @return 公文实体
     */
    @GetMapping("/{id}")
    public CommonResult<MissiveEntity> getMissiveById(@PathVariable Long id) {
        return ResultGenerator.genOkResult(missiveService.getById(id));
    }

    /**
     * 新增 公文 实体
     * @param req 实体参数
     * @return true
     */
    @PostMapping
    public CommonResult<Boolean> addMissive(@Valid @RequestBody AddMissiveReq req) {
        MissiveEntity entity = new MissiveEntity();
        BeanUtil.copyProperties(req, entity);
        missiveService.add(entity);
        return ResultGenerator.genOkResult(true);
    }

    /**
     * 更新 公文 实体
     * @param req 实体参数
     * @return true
     */
    @PutMapping
    public CommonResult<Boolean> updateMissive(@Valid @RequestBody UpdateMissiveReq req) {
        MissiveEntity entity = new MissiveEntity();
        BeanUtil.copyProperties(req, entity);
        missiveService.update(entity);
        return ResultGenerator.genOkResult(true);
    }

    /**
     * 删除 公文 实体
     * @param id 实体id
     * @return true
     */
    @DeleteMapping("/{id}")
    public CommonResult<Boolean> deleteMissiveById(@PathVariable Long id) {
        missiveService.deleteById(id);
        return ResultGenerator.genOkResult(true);
    }

}
