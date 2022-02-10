package com.wangyang.bioinfo.pojo.entity.geo;

import com.univocity.parsers.annotations.Parsed;
import com.wangyang.bioinfo.handle.ITaskEntity;
import com.wangyang.bioinfo.pojo.annotation.QueryField;
import com.wangyang.bioinfo.pojo.entity.base.BaseEntity;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Entity(name = "t_gpl_support")
@Data
public class GPLSupport extends BaseEntity {

//    @Id
//    @GeneratedValue(strategy = GenerationType.AUTO, generator = "myid")
//    @GenericGenerator(name = "myid", strategy = "com.wangyang.bioinfo.support.ManualInsertGenerator")
//    @Parsed
//    private Integer id;

    @Parsed
    @QueryField
    private String gpl;
    @Parsed
    private Integer rowNum;
    @Parsed
    private Integer sampleCount;
    @Parsed
    private Integer seriesCount;
    @Column(columnDefinition = "longtext")
    private String description;
    @Parsed
    private String gplPath;
    @Parsed
    private String probeId;
    @Parsed
    private String symbol;
    @Parsed
    private String annotationPath;

}
