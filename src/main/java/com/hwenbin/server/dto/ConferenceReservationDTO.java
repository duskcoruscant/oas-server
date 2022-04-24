package com.hwenbin.server.dto;

import lombok.Data;

import java.time.LocalDate;
import java.util.List;

/**
 * 会议预订DTO
 *
 * @author hwb
 * @date 2022/04/20 22:50
 */
@Data
public class ConferenceReservationDTO {

    /**
     * 自增id
     */
    private Long id;

    /**
     * 会议室编号
     */
    private String roomCode;

    /**
     * 会议预订日期
     */
    private LocalDate date;

    /**
     * 当天该会议室所有已预订时间列表
     */
    private List<ReservationTime> resTimes;

    @Data
    public static class ReservationTime {

        /**
         * 会议开始时间 hh(/h):mm
         */
        private String startTime;

        /**
         * 会议结束时间 hh(/h):mm
         */
        private String endTime;

        /**
         * 预订人姓名
         */
        private String resEmpName;

    }

}
