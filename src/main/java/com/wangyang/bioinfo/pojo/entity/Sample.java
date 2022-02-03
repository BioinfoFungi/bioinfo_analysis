package com.wangyang.bioinfo.pojo.entity;



import com.wangyang.bioinfo.pojo.entity.base.BaseEntity;
import lombok.Data;

import javax.persistence.*;

/**
 * @author wangyang
 * @date 2021/6/28
 */
@Data
@Entity(name = "t_sample")
public class Sample extends BaseEntity {
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private int id;
    private String name;
//    @Convert(converter = JpaConverterJson.class)
//    @Column(columnDefinition = "TEXT")
//    private Integer[] geneData;
}
