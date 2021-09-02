package com.wangyang.bioinfo.pojo.vo;

import com.wangyang.bioinfo.pojo.enums.CodeType;
import com.wangyang.bioinfo.pojo.enums.TaskType;
import lombok.Data;

@Data
public class CodeVO extends TermMappingVo{
    private String code;
    private String name;
    private CodeType codeType;
    private TaskType taskType;
}
