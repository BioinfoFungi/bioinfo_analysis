package com.wangyang.bioinfo.pojo.trem;

import com.wangyang.bioinfo.pojo.base.BaseEntity;
import com.wangyang.bioinfo.pojo.base.BaseTerm;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

/**
 * @author wangyang
 * @date 2021/6/26
 */
@Entity
@DiscriminatorValue(value = "0")
@Data
public class DataOrigin extends BaseTerm {

}
