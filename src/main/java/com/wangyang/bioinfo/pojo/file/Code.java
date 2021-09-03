package com.wangyang.bioinfo.pojo.file;

import com.univocity.parsers.annotations.Parsed;
import com.wangyang.bioinfo.pojo.annotation.QueryField;
import com.wangyang.bioinfo.pojo.enums.CodeType;
import com.wangyang.bioinfo.pojo.enums.TaskType;
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
    @Parsed
    private String name;
    @Parsed
    private CodeType codeType= CodeType.R;
    @Parsed
    private TaskType taskType=TaskType.CANCER_STUDY;
    @QueryField
    @Parsed
    private Boolean haveParentId;
    @QueryField
    @Parsed
    private Boolean haveExpr;
    @QueryField
    @Parsed
    private Boolean haveMetadata;

//    private Integer changeCancerId;
//    private Integer changeStudyId;
//    private Integer changeDataOriginId;
//    private Integer changeAnalysisSoftwareId;
//    private Integer changeDataCategoryId;
}
