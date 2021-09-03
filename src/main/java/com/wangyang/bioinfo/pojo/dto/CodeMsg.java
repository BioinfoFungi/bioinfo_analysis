package com.wangyang.bioinfo.pojo.dto;

import com.wangyang.bioinfo.pojo.file.CancerStudy;
import com.wangyang.bioinfo.pojo.param.CancerStudyParam;
import lombok.Data;

import java.util.Map;

@Data
public class CodeMsg {
    private String runMsg;
    private String exception;
    private Boolean status;
    private Map<String,String> resultMap;
    private String result;
    private Boolean isUpdate=false;
    private String sourceCode;
}
