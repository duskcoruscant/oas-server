package com.hwenbin.server.controller.filecenter;

import com.hwenbin.server.core.web.response.CommonResult;
import com.hwenbin.server.core.web.response.PageParam;
import com.hwenbin.server.core.web.response.PageResult;
import com.hwenbin.server.core.web.response.ResultGenerator;
import com.hwenbin.server.dto.RecycleFileDTO;
import com.hwenbin.server.service.RecycleFileService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.validation.Valid;

/**
 * 回收站控制器
 *
 * @author hwb
 * @date 2022/04/07 18:19
 */
@RestController
@RequestMapping("/recycleFile")
public class RecycleFileController {

    @Resource
    private RecycleFileService recycleFileService;

    /**
     * 回收站 分页查询
     * @param empId 员工id
     * @param pageParam 分页参数
     * @return 分页结果
     */
    @GetMapping
    public CommonResult<PageResult<RecycleFileDTO>> pageQueryForRecycleFile(@RequestParam Long empId,
                                                                            @Valid PageParam pageParam) {
        return ResultGenerator.genOkResult(recycleFileService.pageQuery(empId, pageParam));
    }

}
