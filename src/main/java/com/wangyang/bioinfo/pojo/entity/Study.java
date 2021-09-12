package com.wangyang.bioinfo.pojo.entity;

import com.wangyang.bioinfo.pojo.entity.base.BaseTerm;
import lombok.Data;

import javax.persistence.*;

/**
 * 关于癌症的研究类型
 * @author wangyang
 * @date 2021/6/26
 */
@Entity(name = "t_study")
//@DiscriminatorValue(value = "1")
@Data
public class Study extends BaseTerm {

}
