package com.wangyang.bioinfo.pojo;

import lombok.Data;

import javax.persistence.MappedSuperclass;

/**
 * @author wangyang
 * @date 2021/6/26
 */
@MappedSuperclass
@Data
public class BaseFile extends BaseEntity{
    private String fileName;
    private String fileType;
    private String path;
    private Long size;
}
