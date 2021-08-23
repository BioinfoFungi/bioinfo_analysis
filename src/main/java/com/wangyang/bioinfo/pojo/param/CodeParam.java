package com.wangyang.bioinfo.pojo.param;

import com.univocity.parsers.annotations.Parsed;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class CodeParam extends TermMappingParam{
    @Parsed
    private String name;
    private Integer change_cancerId;
    private Integer change_studyId;
    private Integer change_dataOriginId;
    private Integer change_analysisSoftwareId;
    private Integer change_dataCategoryId;
}
