package com.wangyang.bioinfo.pojo.param;

import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @author wangyang
 * @date 2021/7/17
 */
@Data
public class BaseFileParam {
    private String absolutePath;
    private String relativePath;
}
