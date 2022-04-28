package com.hwenbin.server.dto;

import com.hwenbin.server.entity.WorkLogEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * @author hwb
 * @date 2022/04/26 11:55
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class WorkLogDTO extends WorkLogEntity {

    /**
     * 通知人id集合
     */
    private Set<Long> sendEmpIds;

    /**
     * 通知人集合
     */
    private Set<Long> sendEmpNames;

    /**
     * 已读人姓名集合
     */
    private Set<Long> readEmpNames;

    /**
     * 评论数量
     */
    private Integer commentCount;

    /**
     * 评论列表
     */
    private List<CommentDetail> comments;

    @Data
    public static class CommentDetail {

        /**
         * 评论时间
         */
        private Date time;

        /**
         * 评论人
         */
        private String name;

        /**
         * 评论内容
         */
        private String content;

    }

}
