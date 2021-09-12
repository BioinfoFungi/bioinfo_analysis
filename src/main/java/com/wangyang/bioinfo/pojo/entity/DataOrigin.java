package com.wangyang.bioinfo.pojo.entity;

import com.wangyang.bioinfo.pojo.entity.base.BaseTerm;
import lombok.Data;

import javax.persistence.*;

/**
 * @author wangyang
 * @date 2021/6/26
 */
@Entity(name = "t_data_origin")
//@DiscriminatorValue(value = "0")
@Data
public class DataOrigin extends BaseTerm {

}
