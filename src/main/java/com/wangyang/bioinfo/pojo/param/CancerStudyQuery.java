package com.wangyang.bioinfo.pojo.param;

import lombok.Data;

/**
 * @author wangyang
 * @date 2021/6/26
 */
@Data
public class CancerStudyQuery extends TermMappingParam {
    private Integer cancerId;
    private Integer studyId;
    private Integer dataOriginId;
    private Integer analysisSoftwareId;
    private Integer dataCategoryId;
    private Integer annotationId;
    private String keyword;
    private Integer parentId;

}
