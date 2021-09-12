package com.wangyang.bioinfo.pojo.param;

import com.univocity.parsers.annotations.Convert;
import com.univocity.parsers.annotations.Parsed;
import com.wangyang.bioinfo.pojo.dto.term.BaseFileDTO;
import com.wangyang.bioinfo.pojo.enums.CodeType;
import com.wangyang.bioinfo.pojo.enums.TaskType;
import com.wangyang.bioinfo.support.UnivocityConverterStr2IntSet;
import com.wangyang.bioinfo.support.UnivocityConverterStr2StrSet;
import lombok.Data;


import java.util.Set;

@Data
public class CodeParam extends BaseFileDTO {
    @Parsed
    private String name;
    @Parsed
    private CodeType codeType;
    @Parsed
    private TaskType taskType=TaskType.CANCER_STUDY;
    @Parsed
    private Integer prerequisites=-1;
    @Parsed
    private Integer id;
    @Parsed
    private String codeOutput;
    @Parsed
    @Convert(conversionClass = UnivocityConverterStr2IntSet.class)
    private Set<Integer> cancer;
    @Parsed
    @Convert(conversionClass = UnivocityConverterStr2IntSet.class)
    private Set<Integer> study;
    @Parsed
    @Convert(conversionClass = UnivocityConverterStr2IntSet.class)
    private Set<Integer> dataOrigin;
    @Parsed
    @Convert(conversionClass = UnivocityConverterStr2IntSet.class)
    private Set<Integer> analysisSoftware;
    @Parsed
    @Convert(conversionClass = UnivocityConverterStr2IntSet.class)
    private Set<Integer> dataCategory;

    @Parsed
    @Convert(conversionClass = UnivocityConverterStr2StrSet.class)
    private Set<String> cancerStr;
    @Parsed
    @Convert(conversionClass = UnivocityConverterStr2StrSet.class)
    private Set<String> studyStr;
    @Parsed
    @Convert(conversionClass = UnivocityConverterStr2StrSet.class)
    private Set<String> dataOriginStr;
    @Parsed
    @Convert(conversionClass = UnivocityConverterStr2StrSet.class)
    private Set<String> analysisSoftwareStr;
    @Parsed
    @Convert(conversionClass = UnivocityConverterStr2StrSet.class)
    private Set<String> dataCategoryStr;


}
