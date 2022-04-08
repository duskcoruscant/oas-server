package com.hwenbin.server.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

/**
 * 回收站文件
 *
 * @author hwb
 * @date 2022/04/07 18:11
 */
@TableName("recycle_file")
@Data
public class RecycleFile {

    /**
     * 自增id
     */
    @TableId
    private Long id;

    /**
     * 员工id
     */
    private Long empId;

    /**
     * 文件id
     */
    private Long fileId;

    /**
     * 删除时间
     * 用途：主要用来计算是否超过自动清除时间，超过则物理删除记录
     */
    private Date deletedTime;

    /**
     * 删除批次号
     * 用途：回收站中，恢复 和 彻底删除 功能，根据此字段可快速找到file表中需要被操作的所有文件
     */
    private String deletedBatchNum;

}
