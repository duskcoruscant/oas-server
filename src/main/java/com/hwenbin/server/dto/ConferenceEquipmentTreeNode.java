package com.hwenbin.server.dto;

import lombok.Data;

import java.util.List;

/**
 * @author hwb
 * @date 2022/04/14 22:49
 */
@Data
public class ConferenceEquipmentTreeNode {

    private Long value;

    private String label;

    private Boolean disabled;

    private List<ConferenceEquipmentTreeNode> children;

}
