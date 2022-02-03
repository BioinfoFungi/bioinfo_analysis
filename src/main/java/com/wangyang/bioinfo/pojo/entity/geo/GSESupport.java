package com.wangyang.bioinfo.pojo.entity.geo;

import com.univocity.parsers.annotations.Parsed;
import com.wangyang.bioinfo.handle.ITaskEntity;
import com.wangyang.bioinfo.pojo.entity.base.TermMapping;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;

@Entity(name = "t_gse_support")
@Data
public class GSESupport extends TermMapping implements ITaskEntity {
    @Parsed
    private String gse;
    @Parsed
    private String gpl;
    @Parsed
    private String pubMed;
    private String gsePath;
    @Parsed
    private String species;
    @Column(columnDefinition = "longtext")
    private String description;
}
