package com.wangyang.bioinfo.pojo.param;

import com.univocity.parsers.annotations.Parsed;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @author wangyang
 * @date 2021/6/26
 */
@Data
public class CancerStudyParam  extends BaseFileParam{
    @NotBlank(message = "cancer不能为空!")
    private String cancer;
    @NotBlank(message = "study不能为空!")
    private String study;
    @NotBlank(message = "dataOrigin不能为空!")
    private String dataOrigin;
    @NotBlank(message = "experimentalStrategy不能为空!")
    private String experimentalStrategy;
    @NotBlank(message = "analysisSoftware不能为空!")
    private String analysisSoftware;

    private String processedAbsolutePath;
    private String processedRelativePath;
    private Integer processedId;
}
