package com.wangyang.bioinfo.pojo.enums;


import com.fasterxml.jackson.annotation.JsonValue;

public enum TaskType {
    CANCER_STUDY(0,"CANCER_STUDY"),
    ANNOTATION(1,"ANOTHER"),
    TEST(2,"TEST");
    private final  String name;
    private final   int code;

    TaskType(int code,String name) {
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
