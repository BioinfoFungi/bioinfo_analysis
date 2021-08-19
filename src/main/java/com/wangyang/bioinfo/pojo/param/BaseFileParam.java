package com.wangyang.bioinfo.pojo.param;

import com.univocity.parsers.annotations.Parsed;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @author wangyang
 * @date 2021/7/17
 */
@Data
public class BaseFileParam {
    @Parsed
    private String absolutePath;
    @Parsed
    private String relativePath;
}
