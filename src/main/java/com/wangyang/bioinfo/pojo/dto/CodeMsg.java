package com.wangyang.bioinfo.pojo.dto;

import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class CodeMsg {
    private String runMsg;
    private String exception;
    private Boolean status;
    private List<Map<String,String>> resultMap;
    private String result;
    private Boolean isUpdate=false;
    private String sourceCode;

}
