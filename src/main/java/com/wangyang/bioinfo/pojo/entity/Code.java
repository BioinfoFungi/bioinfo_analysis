package com.wangyang.bioinfo.pojo.entity;

import com.univocity.parsers.annotations.Convert;
import com.univocity.parsers.annotations.Parsed;
import com.wangyang.bioinfo.pojo.entity.base.BaseFile;
import com.wangyang.bioinfo.pojo.enums.CodeType;
import com.wangyang.bioinfo.pojo.enums.TaskType;
import com.wangyang.bioinfo.support.JpaConverterJson;
import com.wangyang.bioinfo.support.UnivocityConverter;
import lombok.Data;

import javax.persistence.Column;
//import javax.persistence.Convert;
import javax.persistence.Entity;
import java.util.Set;

/**
 * @author wangyang
 * @date 2021/7/22
 */
@Data
@Entity(name = "t_code")
//@DiscriminatorValue(value = "2")
public class Code extends BaseFile {
    @Column(columnDefinition = "longtext")
    private String code;
    @Parsed
    private String name;
    @Parsed
    private CodeType codeType= CodeType.R;
    @Parsed
    private TaskType taskType=TaskType.CANCER_STUDY;
    @Parsed
    private Integer prerequisites;
    @Column(columnDefinition = "longtext")
    @Parsed
    private String codeOutput;
    @Parsed
    @javax.persistence.Convert(converter = JpaConverterJson.class)
    @com.univocity.parsers.annotations.Convert(conversionClass = UnivocityConverter.class)
    private Set<Integer> cancer;
    @Parsed
    @javax.persistence.Convert(converter = JpaConverterJson.class)
    @com.univocity.parsers.annotations.Convert(conversionClass = UnivocityConverter.class)
    private Set<Integer> study;
    @Parsed
    @javax.persistence.Convert(converter = JpaConverterJson.class)
    @com.univocity.parsers.annotations.Convert(conversionClass = UnivocityConverter.class)
    private Set<Integer> dataOrigin;
    @Parsed
    @javax.persistence.Convert(converter = JpaConverterJson.class)
    @com.univocity.parsers.annotations.Convert(conversionClass = UnivocityConverter.class)
    private Set<Integer> analysisSoftware;
    @Parsed
    @javax.persistence.Convert(converter = JpaConverterJson.class)
    @com.univocity.parsers.annotations.Convert(conversionClass = UnivocityConverter.class)
    private Set<Integer> dataCategory;
}
