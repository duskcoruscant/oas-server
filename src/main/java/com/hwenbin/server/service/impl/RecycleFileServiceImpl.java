package com.hwenbin.server.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hwenbin.server.core.mybatis.query.MyLambdaQueryWrapper;
import com.hwenbin.server.core.web.response.PageParam;
import com.hwenbin.server.core.web.response.PageResult;
import com.hwenbin.server.core.web.response.ResultCode;
import com.hwenbin.server.dto.RecycleFileDTO;
import com.hwenbin.server.entity.RecycleFile;
import com.hwenbin.server.mapper.RecycleFileMapper;
import com.hwenbin.server.service.FileService;
import com.hwenbin.server.service.RecycleFileService;
import com.hwenbin.server.util.AssertUtils;
import com.hwenbin.server.util.MyBatisUtils;
import com.hwenbin.server.util.ThreadPoolUtil;
import io.minio.errors.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author hwb
 * @date 2022/04/07 18:18
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class RecycleFileServiceImpl extends ServiceImpl<RecycleFileMapper, RecycleFile> implements RecycleFileService {

    @Resource
    private RecycleFileMapper recycleFileMapper;

    @Resource
    private FileService fileService;

    @Override
    public PageResult<RecycleFileDTO> pageQuery(Long empId, PageParam pageParam) {
        // 七天前的这一时刻，在此此后的删除时间为有效的回收站文件
        DateTime validTime = DateUtil.offsetDay(new Date(), -7);
        IPage<RecycleFileDTO> pageQuery = recycleFileMapper.pageQuery(MyBatisUtils.buildPage(pageParam),
                new QueryWrapper<RecycleFile>().eq("rf.emp_id", empId)
                        .gt("rf.deleted_time", validTime));
        // 后台线程删除超过有效时间的文件
        deleteExceedRecycleTimeFile(validTime);
        return new PageResult<>(pageQuery.getRecords(), pageQuery.getTotal());
    }

    /**
     * 删除超过回收时间的文件
     * @param recycleTime 有效时间，在此时间之前的应被彻底删除
     */
    private void deleteExceedRecycleTimeFile(DateTime recycleTime) {
        ThreadPoolUtil.submit(() -> {
            // 找到应该被彻底删除的文件
            List<RecycleFile> shouldDeleteFiles =
                    recycleFileMapper.selectList(new MyLambdaQueryWrapper<RecycleFile>()
                            .lt(RecycleFile::getDeletedTime, recycleTime));
            if (CollUtil.isEmpty(shouldDeleteFiles)) {
                return;
            }
            // 获取删除批次号，物理删除file表与recycle_file表的记录，并删除文件服务器内对应的文件
            Set<Long> deletedBatchIds = new HashSet<>();
            Set<String> deletedBatchNums = new HashSet<>();
            shouldDeleteFiles.forEach(recycleFile -> {
                deletedBatchIds.add(recycleFile.getId());
                deletedBatchNums.add(recycleFile.getDeletedBatchNum());
            });
            recycleFileMapper.deleteBatchIds(deletedBatchIds);
            try {
                fileService.realDeleteByBatchNums(deletedBatchNums);
            } catch (Exception e) {
                log.error("清理过期文件时发生异常" + e.getMessage());
            }
        });
    }

    @Override
    public void addRecycleFile(Long fileId, Long empId, String deletedBatchNum) {
        RecycleFile recycleFile = new RecycleFile();
        recycleFile.setEmpId(empId);
        recycleFile.setFileId(fileId);
        recycleFile.setDeletedTime(new Date());
        recycleFile.setDeletedBatchNum(deletedBatchNum);
        recycleFileMapper.insert(recycleFile);
    }

    @Override
    public void deleteById(Long id) throws IOException, InvalidResponseException, InvalidKeyException,
            NoSuchAlgorithmException, ServerException, ErrorResponseException, XmlParserException,
            InvalidBucketNameException, InsufficientDataException, InternalException {
        RecycleFile recycleFile = recycleFileMapper.selectById(id);
        AssertUtils.asserts(recycleFile != null, ResultCode.FILE_NOT_FOUND);
        // 删除记录
        recycleFileMapper.deleteById(id);
        // 根据删除批次号，删除服务器文件
        fileService.realDeleteByBatchNums(ListUtil.of(recycleFile.getDeletedBatchNum()));
    }

    @Override
    public void recoveryById(Long id) {
        RecycleFile recycleFile = recycleFileMapper.selectById(id);
        AssertUtils.asserts(recycleFile != null, ResultCode.FILE_NOT_FOUND);
        // 删除记录
        recycleFileMapper.deleteById(id);
        // 根据删除批次号，将文件表记录中的逻辑删除状态恢复为正常
        fileService.recoveryByDeleteBatchNum(ListUtil.of(recycleFile.getDeletedBatchNum()));
    }

    @Override
    public void deleteAll() throws IOException, InvalidResponseException, InvalidKeyException,
            NoSuchAlgorithmException, ServerException, ErrorResponseException, XmlParserException,
            InvalidBucketNameException, InsufficientDataException, InternalException {
        List<RecycleFile> recycleFiles = recycleFileMapper.selectList();
        if (CollUtil.isEmpty(recycleFiles)) {
            return;
        }
        Set<Long> ids = new HashSet<>();
        Set<String> deletedBatchNums = new HashSet<>();
        recycleFiles.forEach(recycleFile -> {
            ids.add(recycleFile.getId());
            deletedBatchNums.add(recycleFile.getDeletedBatchNum());
        });
        // 清空记录
        recycleFileMapper.deleteBatchIds(ids);
        // 删除文件
        fileService.realDeleteByBatchNums(deletedBatchNums);
    }

}
