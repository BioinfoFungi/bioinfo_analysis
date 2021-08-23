package com.wangyang.bioinfo.pojo.param;

import com.univocity.parsers.annotations.Parsed;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class CodeParam extends TermMappingParam{
    @Parsed
    private String name;
}
