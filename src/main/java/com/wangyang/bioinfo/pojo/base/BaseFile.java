package com.wangyang.bioinfo.pojo.base;

import com.wangyang.bioinfo.pojo.base.BaseEntity;
import lombok.Data;

import javax.persistence.MappedSuperclass;

/**
 * @author wangyang
 * @date 2021/6/26
 */
@MappedSuperclass
@Data
public class BaseFile extends BaseEntity {
    private String fileName;
    private String fileType;
    private String enName;
    private String path;
    private Long size;
}
