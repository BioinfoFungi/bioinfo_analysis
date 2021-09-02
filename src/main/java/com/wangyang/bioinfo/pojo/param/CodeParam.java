package com.wangyang.bioinfo.pojo.param;

import com.univocity.parsers.annotations.Parsed;
import com.wangyang.bioinfo.pojo.enums.CodeType;
import com.wangyang.bioinfo.pojo.enums.TaskType;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class CodeParam extends TermMappingParam{
    @Parsed
    private String name;
    private CodeType codeType;
    private TaskType taskType;

}
