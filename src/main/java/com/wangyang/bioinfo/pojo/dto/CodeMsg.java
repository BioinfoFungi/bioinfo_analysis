package com.wangyang.bioinfo.pojo.dto;

import lombok.Data;

@Data
public class CodeMsg {
    private String[] param;
    private String runMsg;
    private String exception;
}
