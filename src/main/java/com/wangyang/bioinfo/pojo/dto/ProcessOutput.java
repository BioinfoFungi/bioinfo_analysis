package com.wangyang.bioinfo.pojo.dto;

import com.wangyang.bioinfo.pojo.enums.ProcessOutputType;
import lombok.Data;

@Data
public class ProcessOutput {
    private String content;
    private ProcessOutputType type;
    private String name;
}
