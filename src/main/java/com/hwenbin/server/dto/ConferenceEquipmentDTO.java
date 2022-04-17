package com.hwenbin.server.dto;

import com.hwenbin.server.entity.ConferenceEquipmentEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author hwb
 * @date 2022/04/16 19:19
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class ConferenceEquipmentDTO extends ConferenceEquipmentEntity {

    /**
     * 设备所关联的会议室id
     */
    private Long relatedRoomId;

}
