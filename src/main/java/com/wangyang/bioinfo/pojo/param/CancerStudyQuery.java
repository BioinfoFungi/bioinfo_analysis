package com.wangyang.bioinfo.pojo.param;

import com.univocity.parsers.annotations.Parsed;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @author wangyang
 * @date 2021/6/26
 */
@Data
public class CancerStudyQuery {
    private Integer cancerId;

    private Integer studyId;

    private Integer dataOriginId;


    private Integer analysisSoftwareId;

    private Integer dataCategoryId;
//    @NotBlank(message = "cancer不能为空!")
    private String cancer;
//    @NotBlank(message = "study不能为空!")
    private String study;
//    @NotBlank(message = "dataOrigin不能为空!")
    private String dataOrigin;
    private String dataCategory;
    private String analysisSoftware;
    private Integer parentId;
    private String gse;

    private String fileName;
    private String keyword;
    private String uuid;
}
