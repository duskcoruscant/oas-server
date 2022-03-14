package com.hwenbin.server.entity;

import lombok.Data;

import javax.persistence.Transient;
import java.util.List;

/**
 * @author hwb
 * @create 2022-03-14
 */
@Data
public class Resource {

    @Transient
    private String resource;

    @Transient
    private List<Handle> handleList;

}
