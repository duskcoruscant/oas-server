package com.hwenbin.server.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hwenbin.server.controller.filecenter.req.PageQueryForFileReq;
import com.hwenbin.server.core.web.response.PageResult;
import com.hwenbin.server.entity.File;
import io.minio.errors.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Collection;
import java.util.Set;

/**
 * @author hwb
 * @create 2022-04-03
 */
public interface FileService extends IService<File> {

    /**
     * 分页查询
     * @param req 分页参数 + 文件信息参数
     * @return 分页结果
     */
    PageResult<File> pageQuery(PageQueryForFileReq req);

    /**
     * 创建文件夹
     * @param folderName 文件夹名称
     * @param parentId 当前目录id
     * @param empId 创建人id
     * @param isShared 创建共享文件夹
     */
    void createFolder(String folderName, Long parentId, Long empId, Integer isShared);

    /**
     * 下载文件
     * @param fileId 文件id
     * @param httpServletResponse httpServletResponse
     */
    void downloadFileById(Long fileId, HttpServletResponse httpServletResponse) throws UnsupportedEncodingException,
            ServerException, InsufficientDataException, InternalException, InvalidResponseException,
            InvalidKeyException, NoSuchAlgorithmException, XmlParserException, ErrorResponseException, InvalidBucketNameException;

    /**
     * 上传文件
     * @param empId 员工id
     * @param parentId 目录id
     * @param isShared 上传共享文件
     * @param multipartFiles httpServletRequest
     */
    void uploadFile(Long empId, Long parentId, Integer isShared, MultipartFile[] multipartFiles) throws IOException, InvalidKeyException, InvalidResponseException, InsufficientDataException, NoSuchAlgorithmException, ServerException, InternalException, XmlParserException, InvalidBucketNameException, ErrorResponseException, RegionConflictException;

    /**
     * 重命名文件
     * @param id 文件id
     * @param filename 新文件名
     */
    void renameFile(Long id, String filename);

    /**
     * 逻辑删除文件，放入回收站
     * @param id 文件id
     * @param operatorId 操作人
     */
    void removeFileToRecycleBin(Long id, Long operatorId);

    /**
     * 获取id文件夹下的所有子结点（多级） —— 递归
     * @param id 文件id列表
     * @param empId 创建人
     * @param isShare 是否共享文件夹
     * @return 子结点id集合
     */
    Set<Long> getAllChildFileId(Long id, Long empId, Boolean isShare);

    /**
     * 删除文件服务器上存储的文件
     * @param deletedBatchNums 删除批次号列表
     */
    void realDeleteByBatchNums(Collection<String> deletedBatchNums) throws IOException, InvalidKeyException,
            InvalidResponseException, InsufficientDataException, NoSuchAlgorithmException, ServerException,
            InternalException, XmlParserException, InvalidBucketNameException, ErrorResponseException;

    /**
     * 恢复文件的逻辑删除状态为正常
     * @param deletedBatchNums 删除批次号列表
     */
    void recoveryByDeleteBatchNum(Collection<String> deletedBatchNums);

}
