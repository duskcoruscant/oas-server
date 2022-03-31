package com.hwenbin.server.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.hwenbin.server.core.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author hwb
 * @create 2022-03-28
 */
@EqualsAndHashCode(callSuper = true)
@Data
@TableName("position")
public class Position extends BaseEntity {

    /**
     * 职位id
     */
    @TableId
    private Long id;

    /**
     * 职位编码
     */
    private String code;

    /**
     * 职位名称
     */
    private String name;

    /**
     * 显示顺序
     */
    private Integer sort;

    /**
     * 状态
     */
    private Integer status;

    /**
     * 备注
     */
    private String remark;

}
