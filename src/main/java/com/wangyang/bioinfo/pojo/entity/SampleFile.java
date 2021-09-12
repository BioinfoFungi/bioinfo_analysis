package com.wangyang.bioinfo.pojo.entity;

import com.wangyang.bioinfo.pojo.entity.base.TermMapping;
import lombok.Data;

import javax.persistence.Entity;

/**
 * @author wangyang
 * @date 2021/7/25
 */
@Data
@Entity(name = "t_sample_file")
//@DiscriminatorValue(value = "1")
public class SampleFile extends TermMapping {
}
