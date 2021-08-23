package com.wangyang.bioinfo.pojo.vo;

import com.wangyang.bioinfo.pojo.file.CancerStudy;
import lombok.Data;

import javax.persistence.Column;

@Data
public class CancerStudyVO extends TermMappingVo {
    private String gse;
    private String description;
    private Integer parentId;
}
