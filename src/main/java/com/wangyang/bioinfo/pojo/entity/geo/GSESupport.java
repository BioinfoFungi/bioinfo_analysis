package com.wangyang.bioinfo.pojo.entity.geo;

import com.univocity.parsers.annotations.Parsed;
import com.wangyang.bioinfo.handle.ITaskEntity;
import com.wangyang.bioinfo.pojo.annotation.QueryField;
import com.wangyang.bioinfo.pojo.entity.base.TermMapping;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;

@Entity(name = "t_gse_support")
@Data
public class GSESupport extends TermMapping{
    @Parsed
    @QueryField
    private String gse;
    @Parsed
    @QueryField
    private String sra;
    @Parsed
    @QueryField
    private String gseType;
    @Parsed
    private Integer sampleCount;
    @Parsed
    @QueryField
    private String pubMed;
    private String gsePath;

    @Column(columnDefinition = "longtext")
    @Parsed
    @QueryField
    private String description;
    @Parsed
    @QueryField
    private String species;
}
