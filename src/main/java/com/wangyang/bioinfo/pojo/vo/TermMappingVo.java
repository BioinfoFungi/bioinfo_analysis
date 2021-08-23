package com.wangyang.bioinfo.pojo.vo;

import com.wangyang.bioinfo.pojo.file.CancerStudy;
import com.wangyang.bioinfo.pojo.file.TermMapping;
import com.wangyang.bioinfo.pojo.trem.*;
import lombok.Data;

import java.util.Date;

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
