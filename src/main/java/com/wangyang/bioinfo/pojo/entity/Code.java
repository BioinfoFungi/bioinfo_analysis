package com.wangyang.bioinfo.pojo.entity;

import com.univocity.parsers.annotations.Parsed;
import com.wangyang.bioinfo.pojo.entity.base.BaseFile;
import com.wangyang.bioinfo.pojo.enums.CodeType;
import com.wangyang.bioinfo.pojo.enums.CrudType;
import lombok.Data;

import javax.persistence.Column;
//import javax.persistence.Convert;
import javax.persistence.Entity;

/**
 * @author wangyang
 * @date 2021/7/22
 */
@Data
@Entity(name = "t_code")
//@DiscriminatorValue(value = "2")
public class Code extends BaseFile {

    @Column(columnDefinition = "longtext")
    private String content;
    @Parsed
    private String name;
    @Parsed
    private CodeType codeType;
    @Parsed
    private CrudType crudType;
    @Parsed
    private Integer prerequisites;

//    @Parsed
//    @javax.persistence.Convert(converter = JpaConverterJson.class)
//    @com.univocity.parsers.annotations.Convert(conversionClass = UnivocityConverterStr2IntSet.class)
//    private Set<Integer> cancer;
//    @Parsed
//    @javax.persistence.Convert(converter = JpaConverterJson.class)
//    @com.univocity.parsers.annotations.Convert(conversionClass = UnivocityConverterStr2IntSet.class)
//    private Set<Integer> study;
//    @Parsed
//    @javax.persistence.Convert(converter = JpaConverterJson.class)
//    @com.univocity.parsers.annotations.Convert(conversionClass = UnivocityConverterStr2IntSet.class)
//    private Set<Integer> dataOrigin;
//    @Parsed
//    @javax.persistence.Convert(converter = JpaConverterJson.class)
//    @com.univocity.parsers.annotations.Convert(conversionClass = UnivocityConverterStr2IntSet.class)
//    private Set<Integer> analysisSoftware;
//    @Parsed
//    @javax.persistence.Convert(converter = JpaConverterJson.class)
//    @com.univocity.parsers.annotations.Convert(conversionClass = UnivocityConverterStr2IntSet.class)
//    private Set<Integer> dataCategory;
}
