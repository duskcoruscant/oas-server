package com.hwenbin.server.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author hwb
 * @date 2022/04/14 14:17
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("conference_room_equipment")
public class ConferenceRoomEquipmentEntity {

    /**
     * 自增id
     */
    @TableId
    private Long id;

    /**
     * 会议室id
     */
    private Long conferenceRoomId;

    /**
     * 会议室设备id
     */
    private Long conferenceEquipmentId;

}
