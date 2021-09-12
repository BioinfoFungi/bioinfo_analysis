package com.wangyang.bioinfo.pojo.entity;

import com.wangyang.bioinfo.pojo.entity.base.BaseTerm;
import lombok.Data;

import javax.persistence.*;

/**
 * 癌症的分类
 * @author wangyang
 * @date 2021/6/26
 */

//@DiscriminatorValue(value = "2")
@Data
@Entity(name = "t_cancer")
public class Cancer extends BaseTerm {

}
