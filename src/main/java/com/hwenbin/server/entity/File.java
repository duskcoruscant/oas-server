package com.hwenbin.server.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.hwenbin.server.core.constant.ProjectConstant;
import com.hwenbin.server.core.entity.BaseEntity;
import com.hwenbin.server.enums.FileShareTypeEnum;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author hwb
 * @create 2022-04-03
 */
@EqualsAndHashCode(callSuper = true)
@Data
@TableName("file")
public class File extends BaseEntity {

    /**
     * 自增id
     */
    @TableId
    private Long id;

    /**
     * 文件名
     */
    private String name;

    /**
     * 路径
     */
    private String path;

    /**
     * 类型
     */
    private String type;

    /**
     * 大小
     */
    private String size;

    /**
     * 员工id
     */
    private Long empId;

    /**
     * 父级id 0为根目录
     */
    private Long parentId;

    /**
     * 是否公共
     */
    private Integer isShared;

    /**
     * content-type
     */
    private String contentType;

    /**
     * 删除批次号
     */
    private String deletedBatchNum;

    /**
     * 是否是文件夹
     */
    public Boolean isFolder() {
        return ProjectConstant.FOLDER.equals(this.type);
    }

    /**
     * 是否是共享
     */
    public Boolean isShare() {
        return FileShareTypeEnum.IS_SHARED.getValue().equals(this.isShared);
    }

}
