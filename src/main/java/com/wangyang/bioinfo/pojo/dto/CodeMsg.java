package com.wangyang.bioinfo.pojo.dto;

import com.wangyang.bioinfo.pojo.file.CancerStudy;
import com.wangyang.bioinfo.pojo.param.CancerStudyParam;
import lombok.Data;

@Data
public class CodeMsg {
    private String runMsg;
    private String exception;
    private Boolean status;
    private CancerStudyParam cancerStudyParam;
    private String result;
}
