package com.wangyang.bioinfo.pojo.RNA;

import com.wangyang.bioinfo.pojo.base.BaseRNA;
import lombok.Data;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

/**
 * @author wangyang
 * @date 2021/7/10
 */
@Entity(name = "t_circRNA")
@DiscriminatorValue(value = "3")
@Data
public class CircRNA extends BaseRNA {
}
