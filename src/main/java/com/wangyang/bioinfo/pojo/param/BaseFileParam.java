package com.wangyang.bioinfo.pojo.param;

import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @author wangyang
 * @date 2021/7/17
 */
@Data
public class BaseFileParam {
    @NotBlank(message = "enName不能为空!")
    private String enName;
    private String fileName;
    private String absolutePath;
    private String fileType;
    private String relativePath;
    private Integer userId;
    private Boolean isCompress;

}
