package com.hwenbin.server.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hwenbin.server.core.web.response.PageParam;
import com.hwenbin.server.core.web.response.PageResult;
import com.hwenbin.server.dto.RecycleFileDTO;
import com.hwenbin.server.entity.RecycleFile;
import com.hwenbin.server.mapper.RecycleFileMapper;
import com.hwenbin.server.service.RecycleFileService;
import com.hwenbin.server.util.MyBatisUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;

/**
 * @author hwb
 * @date 2022/04/07 18:18
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class RecycleFileServiceImpl extends ServiceImpl<RecycleFileMapper, RecycleFile> implements RecycleFileService {

    @Resource
    private RecycleFileMapper recycleFileMapper;

    @Override
    public PageResult<RecycleFileDTO> pageQuery(Long empId, PageParam pageParam) {
        // TODO : 1、查询七天内删除的  2、提交任务至后台线程，删除超过七天的文件
        IPage<RecycleFileDTO> pageQuery = recycleFileMapper.pageQuery(MyBatisUtils.buildPage(pageParam),
                new QueryWrapper<RecycleFile>().eq("rf.emp_id", empId));
        return new PageResult<>(pageQuery.getRecords(), pageQuery.getTotal());
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

}
