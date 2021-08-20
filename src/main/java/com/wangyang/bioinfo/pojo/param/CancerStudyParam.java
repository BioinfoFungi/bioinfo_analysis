package com.wangyang.bioinfo.pojo.param;

import com.univocity.parsers.annotations.Parsed;
import com.wangyang.bioinfo.pojo.dto.BaseFileDTO;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @author wangyang
 * @date 2021/6/26
 */
@Data
public class CancerStudyParam extends BaseFileDTO {
    @NotBlank(message = "cancer不能为空!")
    @Parsed
    private String cancer;
    @NotBlank(message = "study不能为空!")
    @Parsed
    private String study;
    @NotBlank(message = "dataOrigin不能为空!")
    @Parsed
    private String dataOrigin;
    @Parsed
    private String dataCategory;
    @Parsed
    private String gse;
    @Parsed
    private String analysisSoftware;
    @Parsed
    private Integer parentId;

//    private String processedAbsolutePath;
//    @Parsed
//    private String processedRelativePath;
//    @Parsed
//    private Integer processedId;
}
