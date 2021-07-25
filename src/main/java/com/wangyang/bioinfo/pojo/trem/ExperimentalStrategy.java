package com.wangyang.bioinfo.pojo.trem;

import com.wangyang.bioinfo.pojo.base.BaseTerm;
import lombok.Data;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

/**
 * @author wangyang
 * @date 2021/7/25
 */
@Entity
@DiscriminatorValue(value = "3")
@Data
public class ExperimentalStrategy extends BaseTerm {
}