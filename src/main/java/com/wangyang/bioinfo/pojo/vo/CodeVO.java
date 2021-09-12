package com.wangyang.bioinfo.pojo.vo;

import com.wangyang.bioinfo.pojo.annotation.QueryField;
import com.wangyang.bioinfo.pojo.entity.Code;
import com.wangyang.bioinfo.pojo.enums.CodeType;
import com.wangyang.bioinfo.pojo.enums.TaskType;
import lombok.Data;

@Data
public class CodeVO extends Code {
    private String codeOutput;
}
