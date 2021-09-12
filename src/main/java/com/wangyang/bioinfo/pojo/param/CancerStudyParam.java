package com.wangyang.bioinfo.pojo.param;

import com.univocity.parsers.annotations.Parsed;
import io.swagger.models.auth.In;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.util.List;

/**
 * @author wangyang
 * @date 2021/6/26
 */
@Data
public class CancerStudyParam extends TermMappingParam {
    @Parsed
    private String annotation;
    @Parsed
    private String gse;
    private String metadata;
    private String expr;
    private String description;
    @Parsed
    private String param;
    @Parsed
    private Integer id;
}
