package com.wangyang.bioinfo.pojo.file;

import com.univocity.parsers.annotations.Parsed;
import com.wangyang.bioinfo.pojo.base.BaseFile;
import lombok.Data;

import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.Entity;
import javax.persistence.MappedSuperclass;

/**
 * @author wangyang
 * @date 2021/7/25
 */

@Data
@Entity(name = "t_data_category")
@DiscriminatorColumn(name = "type", discriminatorType = DiscriminatorType.INTEGER, columnDefinition = "int default 0")
public class BaseDataCategory extends BaseFile {
    @Parsed
    private Integer cancerId;
    @Parsed
    private Integer studyId;
    @Parsed
    private Integer dataOriginId;

    @Parsed
    private Integer analysisSoftwareId;
    @Parsed
    private Integer dataCategoryId;
    @Parsed
    private String gse;
}
