package com.wangyang.bioinfo.pojo.param;

import com.wangyang.bioinfo.pojo.annotation.QueryField;
import com.wangyang.bioinfo.pojo.dto.term.TermMappingDTO;
import com.wangyang.bioinfo.pojo.file.Code;
import lombok.Data;

@Data
public class CodeQuery extends TermMappingParam {
    @QueryField
    private Integer cancerId;
    @QueryField
    private Integer studyId;
    @QueryField
    private Integer dataOriginId;
    @QueryField
    private Integer analysisSoftwareId;
    @QueryField
    private Integer dataCategoryId;
    private String keyWard;
}
