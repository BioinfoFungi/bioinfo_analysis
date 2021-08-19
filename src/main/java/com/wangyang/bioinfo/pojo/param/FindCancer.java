package com.wangyang.bioinfo.pojo.param;

import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @author wangyang
 * @date 2021/6/26
 */
@Data
public class FindCancer {
//    @NotBlank(message = "cancer不能为空!")
    private String cancer;
//    @NotBlank(message = "study不能为空!")
    private String study;
//    @NotBlank(message = "dataOrigin不能为空!")
    private String dataOrigin;
    private String dataCategory;
    private String GSE;
    private String analysisSoftware;
    private String fileName;
    private String keyword;
    private String uuid;
}
