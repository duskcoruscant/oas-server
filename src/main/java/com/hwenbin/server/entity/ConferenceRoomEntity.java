package com.hwenbin.server.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.hwenbin.server.core.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author hwb
 * @date 2022/04/13 20:26
 */
@EqualsAndHashCode(callSuper = true)
@Data
@TableName("conference_room")
public class ConferenceRoomEntity extends BaseEntity {

    /**
     * 自增id
     */
    @TableId
    private Long id;

    /**
     * 会议室编号
     */
    private String code;

    /**
     * 会议室地址
     */
    private String address;

    /**
     * 状态（0正常，1停用）
     */
    private Integer status;

}
