package com.wangyang.bioinfo.pojo.entity.geo;

import com.univocity.parsers.annotations.Parsed;
import com.wangyang.bioinfo.handle.ITaskEntity;
import com.wangyang.bioinfo.pojo.annotation.QueryField;
import com.wangyang.bioinfo.pojo.entity.base.BaseEntity;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Entity(name = "t_gsm_support")
@Data
public class GSMSupport extends BaseEntity {
//    @Id
//    @GeneratedValue(strategy = GenerationType.AUTO, generator = "myid")
//    @GenericGenerator(name = "myid", strategy = "com.wangyang.bioinfo.support.ManualInsertGenerator")
//    @Parsed
//    private Integer id;
    @Parsed
    private String gsm;
    @Parsed
    private String gpl;
    @Parsed
    private String gse;
    @Parsed
    private String sra;
    @Parsed
    @QueryField
    private String species;
    @Column(columnDefinition = "longtext")
    @QueryField
    @Parsed
    private String description;
}
