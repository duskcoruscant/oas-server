package com.hwenbin.server.controller.managecenter;

import com.hwenbin.server.controller.managecenter.req.GetPositionListReq;
import com.hwenbin.server.core.web.response.CommonResult;
import com.hwenbin.server.core.web.response.PageResult;
import com.hwenbin.server.core.web.response.ResultGenerator;
import com.hwenbin.server.dto.PositionDTO;
import com.hwenbin.server.entity.Position;
import com.hwenbin.server.service.PositionService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.security.Principal;
import java.util.List;

/**
 * @author hwb
 * @create 2022-03-28
 */
@RestController
@RequestMapping("/position")
public class PositionController {

    @Resource
    private PositionService positionService;

    /**
     * 根据 筛选条件 + 分页，获取职位列表
     * @param req 筛选条件 + 分页参数
     * @return 分页结果
     */
    @PreAuthorize("hasAuthority('manage:position:query')")
    @GetMapping
    public CommonResult<PageResult<Position>> pageQuery(@Valid final GetPositionListReq req) {
        return ResultGenerator.genOkResult(positionService.pageQuery(req));
    }

    /**
     * 获取职位列表，不带分页及搜索，用于下拉列表
     * @param principal 登录信息
     * @return 职位列表
     */
    @GetMapping("/list")
    public CommonResult<List<Position>> getPositionList(final Principal principal) {
        return ResultGenerator.genOkResult(positionService.list());
    }

    /**
     * 新增职位
     * @param positionDTO 职位信息
     * @param principal 登录信息
     * @return true
     */
    @PreAuthorize("hasAuthority('manage:position:add')")
    @PostMapping
    public CommonResult<Boolean> add(@Valid @RequestBody final PositionDTO positionDTO, final Principal principal) {
        positionService.add(positionDTO);
        return ResultGenerator.genOkResult(true);
    }

    /**
     * 更新职位信息
     * @param positionDTO 要更新的信息
     * @param principal 登录信息
     * @return true
     */
    @PreAuthorize("hasAuthority('manage:position:update')")
    @PutMapping
    public CommonResult<Boolean> update(@Valid @RequestBody final PositionDTO positionDTO, final Principal principal) {
        positionService.update(positionDTO);
        return ResultGenerator.genOkResult(true);
    }

    /**
     * 获取职位详细信息
     * @param id 职位id
     * @param principal 登录信息
     * @return Position
     */
    @PreAuthorize("hasAuthority('manage:position:query')")
    @GetMapping("/get")
    public CommonResult<Position> getDetail(@RequestParam final Long id, final Principal principal) {
        return ResultGenerator.genOkResult(positionService.getDetail(id));
    }

    /**
     * 删除职位
     * @param id 职位id
     * @param principal 登录信息
     * @return true
     */
    @PreAuthorize("hasAuthority('manage:position:delete')")
    @DeleteMapping
    public CommonResult<Boolean> delete(@RequestParam final Long id, final Principal principal) {
        positionService.delete(id);
        return ResultGenerator.genOkResult(true);
    }

}
