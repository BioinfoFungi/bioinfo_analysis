package com.wangyang.bioinfo.pojo.enums;

import com.fasterxml.jackson.annotation.JsonValue;

public enum CrudType {
    GSE(0,"GSE"),GSM(1,"GSM"),GPL(2,"GPL");
    private final  String name;
    private final   int code;

    CrudType(int code, String name) {
        this.name = name;
        this.code=code;
    }
    public Integer getCode() {
        return code;
    }
    @JsonValue
    public String getValue() {
        return name;
    }
}
