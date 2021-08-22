package com.wangyang.bioinfo.pojo.dto.cancerstudy;

import com.wangyang.bioinfo.pojo.annotation.QueryField;
import lombok.Data;

@Data
public class TermMappingDTO extends BaseTermMappingDTO{

    private String cancer;
    private String study;
    private String dataOrigin;
    private String dataCategory;
    private String analysisSoftware;

}
