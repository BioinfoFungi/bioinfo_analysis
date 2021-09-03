package com.wangyang.bioinfo.pojo.vo;

import com.wangyang.bioinfo.pojo.file.Annotation;
import com.wangyang.bioinfo.pojo.file.CancerStudy;
import lombok.Data;

import javax.persistence.Column;

@Data
public class CancerStudyVO extends TermMappingVo {
    private String gse;
    private String description;
    private Integer parentId;
    private Integer codeId;
    private Annotation annotation;
    private Integer annotationId;
    private String metadata;
    private String metadataMd5;
    private Long metadataSize;
    private String expr;
    private String exprMd5;
    private Long exprSize;
    private Boolean metadataStatus;
    private Boolean exprStatus;
    private String metadataRelative;
    private String exprRelative;
}
