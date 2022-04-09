package com.hwenbin.server.controller.filecenter;

import com.hwenbin.server.core.web.response.CommonResult;
import com.hwenbin.server.core.web.response.PageParam;
import com.hwenbin.server.core.web.response.PageResult;
import com.hwenbin.server.core.web.response.ResultGenerator;
import com.hwenbin.server.dto.RecycleFileDTO;
import com.hwenbin.server.service.RecycleFileService;
import io.minio.errors.*;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

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

    /**
     * 彻底删除 文件/文件夹
     * @param id 回收站记录id
     * @return true
     */
    @DeleteMapping("/{id}")
    public CommonResult<Boolean> deleteById(@PathVariable Long id) throws IOException, InvalidResponseException,
            InvalidKeyException, NoSuchAlgorithmException, ServerException, InternalException, XmlParserException,
            InvalidBucketNameException, InsufficientDataException, ErrorResponseException {
        recycleFileService.deleteById(id);
        return ResultGenerator.genOkResult(true);
    }

    /**
     * 清空回收站
     * @return true
     */
    @DeleteMapping("/deleteAll")
    public CommonResult<Boolean> deleteAll() throws IOException, InvalidResponseException, InvalidKeyException,
            NoSuchAlgorithmException, ServerException, InternalException, XmlParserException,
            InvalidBucketNameException, InsufficientDataException, ErrorResponseException {
        recycleFileService.deleteAll();
        return ResultGenerator.genOkResult(true);
    }

    /**
     * 恢复文件
     * @param id 回收站记录id
     * @return true
     */
    @PutMapping("/{id}")
    public CommonResult<Boolean> recoveryById(@PathVariable Long id) {
        recycleFileService.recoveryById(id);
        return ResultGenerator.genOkResult(true);
    }

}
