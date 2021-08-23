package com.wangyang.bioinfo.pojo.file;

import com.univocity.parsers.annotations.Parsed;
import com.wangyang.bioinfo.pojo.annotation.QueryField;
import com.wangyang.bioinfo.pojo.enums.CodeType;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

/**
 * @author wangyang
 * @date 2021/7/22
 */
@Data
@Entity(name = "t_code")
@DiscriminatorValue(value = "2")
public class Code extends TermMapping {
    @Column(columnDefinition = "longtext")
    private String code;
    private String name;
    private CodeType codeType;
    private Integer changeCancerId;
    private Integer changeStudyId;
    private Integer changeDataOriginId;
    private Integer changeAnalysisSoftwareId;
    private Integer changeDataCategoryId;
}
