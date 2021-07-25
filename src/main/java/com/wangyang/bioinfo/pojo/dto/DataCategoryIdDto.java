package com.wangyang.bioinfo.pojo.dto;

import com.univocity.parsers.annotations.Parsed;
import lombok.Data;

/**
 * @author wangyang
 * @date 2021/7/25
 */
@Data
public class DataCategoryIdDto {

    private Integer cancerId;
    private Integer studyId;
    private Integer dataOriginId;
    private Integer experimentalStrategyId;
    private Integer analysisSoftwareId;

    public DataCategoryIdDto(Integer cancerId, Integer studyId, Integer dataOriginId, Integer experimentalStrategyId, Integer analysisSoftwareId) {
        this.cancerId = cancerId;
        this.studyId = studyId;
        this.dataOriginId = dataOriginId;
        this.experimentalStrategyId = experimentalStrategyId;
        this.analysisSoftwareId = analysisSoftwareId;
    }

    public DataCategoryIdDto() {
    }
}
