package com.wangyang.bioinfo.pojo.entity;

import com.wangyang.bioinfo.pojo.entity.base.BaseTerm;
import lombok.Data;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

/**
 * @author wangyang
 * @date 2021/7/25
 */
@Entity(name = "t_data_category")
//@DiscriminatorValue(value = "3")
@Data
public class DataCategory extends BaseTerm {
}
