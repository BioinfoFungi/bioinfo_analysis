package com.wangyang.bioinfo.pojo.param;

import com.univocity.parsers.annotations.Parsed;
import com.wangyang.bioinfo.pojo.annotation.QueryField;
import com.wangyang.bioinfo.pojo.enums.CodeType;
import com.wangyang.bioinfo.pojo.enums.TaskType;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class CodeParam extends TermMappingParam{
    @Parsed
    private String name;
    @Parsed
    private CodeType codeType;
    @Parsed
    private TaskType taskType;
    @Parsed
    private Boolean haveParentId;
    @Parsed
    private Boolean haveExpr;
    @Parsed
    private Boolean haveMetadata;
}
