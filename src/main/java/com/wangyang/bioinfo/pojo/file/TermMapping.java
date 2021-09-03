package com.wangyang.bioinfo.pojo.file;

import com.univocity.parsers.annotations.Parsed;
import com.wangyang.bioinfo.pojo.annotation.QueryField;
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
public class TermMapping extends BaseFile {


    @Parsed(field = "cancer")
    @QueryField
    private Integer cancerId;
    @Parsed(field = "study")
    @QueryField
    private Integer studyId;
    @Parsed(field = "dataOrigin")
    @QueryField
    private Integer dataOriginId;

    @Parsed(field = "analysisSoftware")
    @QueryField
    private Integer analysisSoftwareId;
    @Parsed(field = "dataCategory")
    @QueryField
    private Integer dataCategoryId;
}
