package com.wangyang.bioinfo.pojo.param;

import com.univocity.parsers.annotations.Parsed;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @author wangyang
 * @date 2021/6/26
 */
@Data
public class CancerStudyParam extends TermMappingParam {

    @Parsed
    private Integer parentId;
    @Parsed
    private String gse;

//    private String processedAbsolutePath;
//    @Parsed
//    private String processedRelativePath;
//    @Parsed
//    private Integer processedId;
}
