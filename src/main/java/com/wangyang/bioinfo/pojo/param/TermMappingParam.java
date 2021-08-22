package com.wangyang.bioinfo.pojo.dto.term;

import com.univocity.parsers.annotations.Parsed;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class TermMappingParamDTO extends BaseFileDTO{
    @NotBlank(message = "cancer不能为空!")
    @Parsed
    private String cancer;
    @NotBlank(message = "study不能为空!")
    @Parsed
    private String study;
    @NotBlank(message = "dataOrigin不能为空!")
    @Parsed
    private String dataOrigin;
    @Parsed
    private String dataCategory;

    @Parsed
    private String analysisSoftware;
}
