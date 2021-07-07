package com.wangyang.bioinfo.pojo.entity;



import com.wangyang.bioinfo.pojo.base.BaseEntity;
import com.wangyang.bioinfo.util.JpaConverterListJson;
import lombok.Data;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import javax.validation.constraints.Max;
import java.util.List;

/**
 * @author wangyang
 * @date 2021/6/28
 */
@Data
@Entity(name = "t_sample")
public class Sample extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String name;
    @Convert(converter = JpaConverterListJson.class)
    @Column(columnDefinition = "TEXT")
    private Integer[] geneData;
}
