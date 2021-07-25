package com.wangyang.bioinfo.pojo.file;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

/**
 * @author wangyang
 * @date 2021/7/25
 */
@Data
@Entity
@DiscriminatorValue(value = "1")
public class SampleFile extends BaseDataCategory{
}
