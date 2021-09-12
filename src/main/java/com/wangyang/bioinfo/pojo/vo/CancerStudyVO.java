package com.wangyang.bioinfo.pojo.vo;

import lombok.Data;

@Data
public class CancerStudyVO extends TermMappingVo {
    private String gse;
    private String description;
    private Integer parentId;
    private Integer codeId;
    private String param;
    private String annotation;
    private Boolean codeIsSuccess;
}
