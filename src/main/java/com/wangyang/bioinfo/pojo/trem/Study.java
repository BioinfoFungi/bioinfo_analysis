package com.wangyang.bioinfo.pojo.trem;

import com.wangyang.bioinfo.pojo.base.BaseEntity;
import com.wangyang.bioinfo.pojo.base.BaseTerm;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

/**
 * 关于癌症的研究类型
 * @author wangyang
 * @date 2021/6/26
 */
@Entity
@DiscriminatorValue(value = "1")
@Data
public class Study extends BaseTerm {

}
