package com.hwenbin.server.service.impl;

import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.date.DateUnit;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.IdUtil;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hwenbin.server.controller.filecenter.req.PageQueryForFileReq;
import com.hwenbin.server.core.constant.ProjectConstant;
import com.hwenbin.server.core.mybatis.query.MyLambdaQueryWrapper;
import com.hwenbin.server.core.web.response.PageResult;
import com.hwenbin.server.core.web.response.ResultCode;
import com.hwenbin.server.entity.File;
import com.hwenbin.server.enums.DeleteStatusEnum;
import com.hwenbin.server.enums.FileShareTypeEnum;
import com.hwenbin.server.mapper.FileMapper;
import com.hwenbin.server.service.FileService;
import com.hwenbin.server.service.RecycleFileService;
import com.hwenbin.server.util.AssertUtils;
import com.hwenbin.server.util.ThreadPoolUtil;
import io.minio.*;
import io.minio.errors.*;
import io.minio.messages.DeleteError;
import io.minio.messages.DeleteObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.*;
import java.util.stream.Collectors;

import static com.hwenbin.server.enums.FileShareTypeEnum.IS_SHARED;
import static com.hwenbin.server.enums.FileShareTypeEnum.NONE_SHARED;

/**
 * @author hwb
 * @create 2022-04-03
 */
@Service
@Transactional(rollbackFor = Exception.class)
@Slf4j
public class FileServiceImpl extends ServiceImpl<FileMapper, File> implements FileService {

    @Resource
    private FileMapper fileMapper;

    @Resource
    private RecycleFileService recycleFileService;

    @Resource
    private MinioClient minioClient;

    @Value("${minio.file-bucket-name}")
    private String bucketName;

    @Override
    public PageResult<File> pageQuery(PageQueryForFileReq req) {
        boolean queryShare = req.getIsShared() != null
                && Objects.equals(req.getIsShared(), IS_SHARED.getValue());
        PageResult<File> filePageResult = fileMapper.selectPage(req,
                new MyLambdaQueryWrapper<File>()
                        .likeIfPresent(File::getName, req.getName())
                        .eqIfPresent(File::getEmpId, req.getEmpId())
                        // .eq(File::getIsShared, req.getIsShared() == null ? NONE_SHARED.getValue() : req.getIsShared())
                        // .eq(File::getParentId, req.getParentId())
                        // 查询个人
                        .func(!queryShare, personal ->
                                personal.eq(File::getIsShared, NONE_SHARED.getValue())
                                        .eq(File::getParentId, req.getParentId())
                        )
                        // 查询共享
                        .func(queryShare, share ->
                                share
                                        .apply("IF(mount_share_folder_id IS NOT NULL, mount_share_folder_id, " +
                                        "IF(is_shared = {0}, parent_id, NULL)) = {1}", IS_SHARED.getValue(), req.getParentId())
                        )
        );
        ListUtil.sort(filePageResult.getList(), (a, b) -> a.isFolder().equals(b.isFolder()) ? 0 : a.isFolder() ? -1 : 1);
        return filePageResult;
    }

    @Override
    public void createFolder(String folderName, Long parentId, Long empId, Integer isShared) {
        // TODO : 校验当前员工名下的文件夹中是否存在该filename
        File file = new File();
        file.setName(folderName);
        file.setParentId(parentId);
        file.setEmpId(empId);
        file.setType(ProjectConstant.FOLDER);
        file.setSize("-");
        file.setIsShared(isShared == null ? NONE_SHARED.getValue() : isShared);
        fileMapper.insert(file);
    }

    @Override
    public void downloadFileById(Long fileId, HttpServletResponse httpServletResponse)
            throws UnsupportedEncodingException, ServerException, InsufficientDataException, InternalException,
            InvalidResponseException, InvalidKeyException, NoSuchAlgorithmException, XmlParserException,
            ErrorResponseException, InvalidBucketNameException {
        File file = fileMapper.selectById(fileId);
        AssertUtils.asserts(file != null, ResultCode.FILE_NOT_FOUND);
        // Content-Disposition的作用：告知浏览器以何种方式显示响应返回的文件，用浏览器打开还是以附件的形式下载到本地保存
        // attachment表示以附件方式下载 inline表示在线打开 "Content-Disposition: inline; filename=文件名.mp3"
        // filename表示文件的默认名称，因为网络传输只支持URL编码的相关字符，因此需要将文件名URL编码后进行传输,前端收到后需要反编码才能获取到真正的名称
        // encode后replace替换"+"  解决空格问题
        // 已经判断file不为null，忽略提示
        String fileName = URLEncoder.encode(file.getName(), "UTF-8");
        httpServletResponse.setHeader("Content-Disposition", "attachment;" + "filename=" + fileName.replace("+", "%20"));
        httpServletResponse.setHeader("content-type", file.getContentType());
        httpServletResponse.setContentType("application/octet-stream");
        try (InputStream in = minioClient.getObject(GetObjectArgs.builder().bucket(bucketName).object(file.getPath()).build());
             OutputStream out = httpServletResponse.getOutputStream()) {
            IoUtil.copy(in, out);
        } catch (IOException e) {
            log.error("Download file error", e);
        }
    }

    @Override
    public void uploadFile(Long empId, Long parentId, Integer isShared, MultipartFile[] multipartFiles) throws IOException,
            InvalidKeyException, InvalidResponseException, InsufficientDataException, NoSuchAlgorithmException,
            ServerException, InternalException, XmlParserException, InvalidBucketNameException, ErrorResponseException,
            RegionConflictException {

        if (ArrayUtil.isEmpty(multipartFiles)) {
            return;
        }

        // 判断bucket是否存在，不存在则创建
        boolean isBucketExist = minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucketName).build());
        if (!isBucketExist) {
            minioClient.makeBucket(MakeBucketArgs.builder().bucket(bucketName).build());
        }

        Date date = new Date();
        // 文件名前加上时间戳，解决前端展示不同文件夹的相同文件名冲突覆盖问题（由于当天上传的文件会在同一个Linux文件夹下）
        String basePath = empId + DateUtil.format(date, ProjectConstant.FORMAT_YEAR_MONTH_DAY_WITH_FILE_SEPARATOR)
                + DateUtil.beginOfDay(date).between(date, DateUnit.MS);
        List<File> fileList = new ArrayList<>();
        for (MultipartFile multipartFile : multipartFiles) {
            String filename = multipartFile.getOriginalFilename();
            String filePath = basePath + filename;
            File file = new File();
            file.setEmpId(empId);
            file.setParentId(parentId);
            file.setIsShared(isShared == null ? NONE_SHARED.getValue() : isShared);
            file.setName(filename);
            file.setType(FileUtil.extName(filename));
            file.setPath(filePath);
            file.setSize(formatSize(multipartFile.getSize()));
            file.setContentType(multipartFile.getContentType());
            minioClient.putObject(PutObjectArgs.builder().bucket(bucketName).object(filePath)
                    .stream(multipartFile.getInputStream(), multipartFile.getSize(), -1)
                    .contentType(multipartFile.getContentType()).build());
            fileList.add(file);
        }

        fileMapper.insertBatch(fileList);
    }

    @Override
    public void renameFile(Long id, String filename) {
        // TODO : 校验当前员工名下的 文件 / 文件夹（分情况） 中是否存在该filename
        File file = new File();
        file.setId(id);
        file.setName(filename);
        fileMapper.updateById(file);
    }

    @Override
    public void removeFileToRecycleBin(Long id, Long operatorId) {
        // 校验文件是否存在
        File file = fileMapper.selectById(id);
        AssertUtils.asserts(file != null, ResultCode.FILE_NOT_FOUND);
        // 生成删除批次号
        String deletedBatchNum = IdUtil.fastUUID();
        // 逻辑删除
        fileMapper.update(file,
                new LambdaUpdateWrapper<File>()
                        .set(File::getIsDeleted, DeleteStatusEnum.DELETED.getValue())
                        .set(File::getDeletedBatchNum, deletedBatchNum)
                        .eq(File::getId, id)
        );
        // 在回收站中创建记录，operatorId不为空表示删除操作发生在共享文件，此时放入操作人的回收站中
        // 已经判断 file != null
        recycleFileService.addRecycleFile(id, operatorId != null ? operatorId : file.getEmpId(), deletedBatchNum);
        // 如果为文件夹，需要逻辑删除其下的所有内容（多级），并设置删除批次号
        if (file.isFolder()) {
            markDeleteAllChildByFileId(id, deletedBatchNum, file.getEmpId(), file.isShare());
        }
    }

    @Override
    public Set<Long> getAllChildFileId(Long id, Long empId, Boolean isShare) {
        List<File> fileList;
        if (isShare) {
            // 获取所有共享文件
            fileList = fileMapper.selectList(File::getIsShared, FileShareTypeEnum.IS_SHARED.getValue());
        } else {
            // 拿到创建人，查询其名下所有文件
            fileList = fileMapper.selectList(File::getEmpId, empId);
        }
        // 递归构造id文件夹下的文件列表（多级）
        Set<Long> childIds = new HashSet<>();
        recursive(fileList, childIds, id, Integer.MAX_VALUE);
        return childIds;
    }

    @Override
    public void realDeleteByBatchNums(Collection<String> deletedBatchNums) throws IOException, InvalidKeyException,
            InvalidResponseException, InsufficientDataException, NoSuchAlgorithmException, ServerException,
            InternalException, XmlParserException, InvalidBucketNameException, ErrorResponseException {
        // 获取所有需要删除的文件路径
        List<File> fileList = fileMapper.selectListByDeleteBatchNums(deletedBatchNums);
        Iterable<Result<DeleteError>> results =
                minioClient.removeObjects(RemoveObjectsArgs.builder().bucket(bucketName).objects(
                        fileList.stream().filter(file -> file != null && !file.isFolder())
                                .map(file -> new DeleteObject(file.getPath()))
                                .collect(Collectors.toList())
                ).build());
        for (Result<DeleteError> result : results) {
            final DeleteError deleteError = result.get();
            log.error(
                    "Error in deleting object " + deleteError.objectName() + "; " + deleteError.message()
            );

        }
    }

    @Override
    public void recoveryByDeleteBatchNum(Collection<String> deletedBatchNums) {
        fileMapper.updateDeleteStatusByDeleteBatchNums(deletedBatchNums);
        fileMapper.update(null,
                Wrappers.<File>lambdaUpdate()
                        .set(File::getIsDeleted, DeleteStatusEnum.NONE_DELETED.getValue())
                        .set(File::getDeletedBatchNum, null)
                        .in(File::getDeletedBatchNum, deletedBatchNums)
        );
    }

    @Override
    public List<File> listAllShareFolder() {
        return fileMapper.selectList(
                new MyLambdaQueryWrapper<File>()
                        .select(File::getId, File::getName, File::getParentId)
                        .eq(File::getType, ProjectConstant.FOLDER)
                        .eq(File::getIsShared, FileShareTypeEnum.IS_SHARED.getValue())
        );
    }

    @Override
    public void shareFileTo(Long fileId, Long mountFolderId) {
        // 校验文件是否存在
        File file = fileMapper.selectById(fileId);
        AssertUtils.asserts(file != null, ResultCode.FILE_NOT_FOUND);
        // 校验挂载的文件夹是否存在
        File mountFolder = fileMapper.selectById(mountFolderId);
        AssertUtils.asserts(file != null, ResultCode.FILE_NOT_FOUND, "选择挂载的共享文件夹不存在");
        // 共享文件
        // 已校验file不为空，忽略提示
        // file.setIsShared(FileShareTypeEnum.IS_SHARED.getValue()); // 暂不更改isShared的值
        file.setMountShareFolderId(mountFolderId);
        fileMapper.updateById(file);
    }

    /**
     * 递归获取所有的子文件id，添加到childIds
     * @param fileList 个人文件列表
     * @param childIds 结果
     * @param parentId 指定的父文件id
     * @param recursiveCount 控制递归次数
     */
    public void recursive(List<File> fileList, Set<Long> childIds, Long parentId, int recursiveCount) {
        if (recursiveCount == 0) {
            return;
        }
        for (File file : fileList) {
            if (parentId.equals(file.getParentId())) {
                childIds.add(file.getId());
                recursive(fileList, childIds, file.getId(), recursiveCount - 1);
            }
        }
    }

    /**
     * 由于父文件夹被移入回收站，需要将其下所有内容也逻辑删除
     * @param fileId 父文件夹id
     * @param deletedBatchNum 删除批次号
     * @param empId 创建人
     * @param isShare 是否共享
     */
    private void markDeleteAllChildByFileId(Long fileId, String deletedBatchNum, Long empId, Boolean isShare) {
        ThreadPoolUtil.submit(() -> {
            fileMapper.update(null,
                    Wrappers.<File>lambdaUpdate()
                            .set(File::getIsDeleted, DeleteStatusEnum.DELETED.getValue())
                            .set(File::getDeletedBatchNum, deletedBatchNum)
                            .in(File::getId, getAllChildFileId(fileId, empId, isShare))
            );
        });
    }

    private String formatSize(long size) {
        if (size < 1024) {
            return size + "B";
        } else if (size < 1048576) {
            return (size >> 10) + "KB";
        } else {
            return (size >> 20) + "MB";
        }
    }

}
