package com.wangyang.bioinfo.pojo.param;

import com.univocity.parsers.annotations.Parsed;
import com.wangyang.bioinfo.pojo.dto.term.BaseFileDTO;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class TermMappingParam extends BaseFileDTO {
//    @NotBlank(message = "cancer不能为空!")
    @Parsed
    private String cancer;
    @Parsed
    private String study;
    @Parsed
    private String dataOrigin;
    @Parsed
    private String dataCategory;

    @Parsed
    private String analysisSoftware;
}
