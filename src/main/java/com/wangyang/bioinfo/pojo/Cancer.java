package com.wangyang.bioinfo.pojo;

import com.wangyang.bioinfo.pojo.base.BaseEntity;
import com.wangyang.bioinfo.pojo.base.BaseTerm;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

/**
 * 癌症的分类
 * @author wangyang
 * @date 2021/6/26
 */
@Entity
@DiscriminatorValue(value = "2")
@Data
public class Cancer extends BaseTerm {

}
