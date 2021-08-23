package com.wangyang.bioinfo.pojo.dto.term;

import com.univocity.parsers.annotations.Parsed;
import lombok.Data;

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
