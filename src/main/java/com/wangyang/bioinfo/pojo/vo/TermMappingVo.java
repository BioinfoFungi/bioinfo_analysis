package com.wangyang.bioinfo.pojo.vo;

import com.wangyang.bioinfo.pojo.entity.*;
import com.wangyang.bioinfo.pojo.entity.base.TermMapping;
import lombok.Data;

/**
 * @author wangyang
 * @date 2021/6/26
 */
@Data
public class TermMappingVo extends TermMapping {
    private Cancer cancer;
    private Study study;
    private AnalysisSoftware analysisSoftware;
    private DataCategory dataCategory;
    private DataOrigin dataOrigin;
}
