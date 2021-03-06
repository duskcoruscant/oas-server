package com.hwenbin.server.controller.filecenter;

import com.hwenbin.server.controller.filecenter.req.*;
import com.hwenbin.server.core.web.response.CommonResult;
import com.hwenbin.server.core.web.response.PageResult;
import com.hwenbin.server.core.web.response.ResultGenerator;
import com.hwenbin.server.entity.File;
import com.hwenbin.server.service.FileService;
import io.minio.errors.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Nullable;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.List;

/**
 * @author hwb
 * @create 2022-04-03
 */
@RestController
@RequestMapping("/file")
public class FileController {

    @Resource
    private FileService fileService;

    /**
     * 文件分页查询
     * @param req 分页参数 + 文件信息参数
     * @return 分页结果
     */
    @GetMapping
    public CommonResult<PageResult<File>> pageQueryForFile(@Valid PageQueryForFileReq req) {
        return ResultGenerator.genOkResult(fileService.pageQuery(req));
    }

    /**
     * 创建文件夹
     * @param req 文件夹参数
     * @return true
     */
    @PostMapping("/createFolder")
    public CommonResult<Boolean> createFolder(@Valid @RequestBody CreateFolderReq req) {
        fileService.createFolder(req.getFilename(), req.getParentId(), req.getEmpId(), req.getIsShared());
        return ResultGenerator.genOkResult(true);
    }

    /**
     * 重命名文件
     * @param req 文件参数
     * @return true
     */
    @PutMapping("/rename")
    public CommonResult<Boolean> renameFile(@Valid @RequestBody RenameFileReq req) {
        fileService.renameFile(req.getId(), req.getFilename());
        return ResultGenerator.genOkResult(true);
    }

    /**
     * 删除文件/文件夹 —— 放入回收站
     * @param id 文件id
     * @param operatorId 操作人
     * @return true
     */
    @DeleteMapping
    public CommonResult<Boolean> delete(@RequestParam Long id, @Nullable @RequestParam Long operatorId) {
        fileService.removeFileToRecycleBin(id, operatorId);
        return ResultGenerator.genOkResult(true);
    }

    /**
     * 上传文件
     * @return true
     */
    @PostMapping("/upload")
    public CommonResult<Boolean> uploadFile(@Valid UploadFileReq req,
                                            @RequestParam MultipartFile[] multipartFiles) throws IOException,
            InvalidResponseException, RegionConflictException, InvalidKeyException, NoSuchAlgorithmException,
            ServerException, ErrorResponseException, XmlParserException, InvalidBucketNameException,
            InsufficientDataException, InternalException {
        fileService.uploadFile(req.getEmpId(), req.getParentId(), req.getIsShared(), multipartFiles);
        return ResultGenerator.genOkResult(true);
    }

    /**
     * 下载文件
     * @param id 文件id
     * @param httpServletResponse httpServletResponse
     */
    @GetMapping("/download/{id}")
    public void downloadFile(@PathVariable @NotNull Long id, HttpServletResponse httpServletResponse)
            throws InvalidKeyException, InvalidResponseException, InsufficientDataException, NoSuchAlgorithmException,
            ServerException, ErrorResponseException, XmlParserException, UnsupportedEncodingException,
            InternalException, InvalidBucketNameException {
        fileService.downloadFileById(id, httpServletResponse);
    }

    /**
     * 获取共享文件夹列表
     * @return 共享文件夹列表
     */
    @GetMapping("/listAllShareFolder")
    public CommonResult<List<File>> listAllShareFolder() {
        return ResultGenerator.genOkResult(fileService.listAllShareFolder());
    }

    /**
     * 个人文件共享至指定共享文件夹中
     * @param req 请求参数
     * @return null
     */
    @PostMapping("/shareFileTo")
    public CommonResult<Void> shareFileTo(@Valid @RequestBody ShareFileToReq req) {
        fileService.shareFileTo(req.getFileId(), req.getMountFolderId());
        return ResultGenerator.genOkResult();
    }

}
