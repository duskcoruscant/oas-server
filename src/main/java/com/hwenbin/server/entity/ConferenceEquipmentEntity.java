package com.hwenbin.server.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.hwenbin.server.core.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 会议室设备实体
 *
 * @author hwb
 * @date 2022/04/13 16:45
 */
@EqualsAndHashCode(callSuper = true)
@Data
@TableName("conference_equipment")
public class ConferenceEquipmentEntity extends BaseEntity {

    /**
     * 自增id
     */
    @TableId
    private Long id;

    /**
     * 会议室设备编号
     */
    private String code;

    /**
     * 会议室设备类型
     */
    private String type;

    /**
     * 会议室设备使用状态（0空闲，1使用中，2损坏）
     */
    private Integer status;

}
