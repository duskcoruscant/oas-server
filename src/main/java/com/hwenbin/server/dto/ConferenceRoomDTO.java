package com.hwenbin.server.dto;

import com.hwenbin.server.entity.ConferenceRoomEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Set;

/**
 * @author hwb
 * @date 2022/04/14 14:02
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class ConferenceRoomDTO extends ConferenceRoomEntity {

    /**
     * 会议室设备编号集合
     */
    private Set<Long> conferenceEquipmentIds;

    /**
     * 会议室设备类型集合
     */
    private Set<String> conferenceEquipmentTypes;

}
