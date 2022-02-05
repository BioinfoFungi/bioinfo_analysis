package com.wangyang.bioinfo.pojo.entity.geo;

import com.univocity.parsers.annotations.Parsed;
import com.wangyang.bioinfo.handle.ITaskEntity;
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
    private String gpl;
    @Parsed
    private Integer rowNum;
    @Column(columnDefinition = "longtext")
    private String description;
    private String gplPath;
}
