package com.wangyang.bioinfo.pojo.file;

import com.univocity.parsers.annotations.Parsed;
import com.wangyang.bioinfo.pojo.base.BaseFile;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

/**
 * 具体的癌症研究
 * @author wangyang
 * @date 2021/6/26
 */
@Entity(name = "t_cancer_study")
@Getter
@Setter
//@DiscriminatorValue(value = "2")
public class CancerStudy extends BaseFile {
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private int id;
    @Parsed
    private int cancerId;
    @Parsed
    private int studyId;
    @Parsed
    private int dataOriginId;
    @Column(columnDefinition = "longtext")
    private String description;
    private String processedAbsolutePath;
    private String processedRelativePath;
    private Integer processedId;
    private Boolean isProcessed;





}
