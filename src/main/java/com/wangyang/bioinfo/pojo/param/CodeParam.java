package com.wangyang.bioinfo.pojo.param;

import com.univocity.parsers.annotations.Parsed;
import com.wangyang.bioinfo.pojo.annotation.QueryField;
import com.wangyang.bioinfo.pojo.dto.term.BaseFileDTO;
import com.wangyang.bioinfo.pojo.enums.CodeType;
import com.wangyang.bioinfo.pojo.enums.TaskType;
import com.wangyang.bioinfo.support.JpaConverterJson;
import lombok.Data;

import javax.persistence.Convert;
import javax.validation.constraints.NotBlank;
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
    private String codeOutput;

    private Set<Integer> cancer;
    private Set<Integer> study;
    private Set<Integer> dataOrigin;
    private Set<Integer> analysisSoftware;
    private Set<Integer> dataCategory;
}
