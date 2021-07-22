package com.wangyang.bioinfo.pojo.file;

import com.wangyang.bioinfo.pojo.base.BaseFile;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;

/**
 * @author wangyang
 * @date 2021/7/22
 */
@Data
@Entity(name = "t_code")
public class Code extends BaseFile {
    @Column(columnDefinition = "longtext")
    private String code;
    private Integer dataOriginId;
    private Integer studyId;
}
