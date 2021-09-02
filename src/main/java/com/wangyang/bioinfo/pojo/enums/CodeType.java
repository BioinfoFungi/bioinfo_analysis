package com.wangyang.bioinfo.pojo.enums;

import com.fasterxml.jackson.annotation.JsonValue;

public enum CodeType {
   R(0,"R"),SHELL(1,"SHELL"),PYTHON(2,"PYTHON");
   private final  String name;
   private final   int code;

   CodeType(int code,String name) {
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
