package com.wangyang.bioinfo.pojo.RNA;

import com.wangyang.bioinfo.pojo.base.BaseRNA;
import lombok.Data;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

/**
 * @author wangyang
 * @date 2021/6/27
 */
@Entity(name = "t_miRNA")
@DiscriminatorValue(value = "1")
@Data
public class MiRNA extends BaseRNA {
}