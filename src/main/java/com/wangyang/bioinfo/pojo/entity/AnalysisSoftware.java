package com.wangyang.bioinfo.pojo.entity;

import com.wangyang.bioinfo.pojo.entity.base.BaseTerm;
import lombok.Data;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

/**
 * @author wangyang
 * @date 2021/7/25
 */
@Entity(name = "t_analysis_software")
//@DiscriminatorValue(value = "4")
@Data
public class AnalysisSoftware extends BaseTerm {
}
