package com.wangyang.bioinfo.pojo.dto;

import com.univocity.parsers.annotations.Parsed;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @author wangyang
 * @date 2021/7/17
 */
@Data
public class BaseFileDTO {
    @Parsed
    private String absolutePath;
    @Parsed
    private String relativePath;
}
