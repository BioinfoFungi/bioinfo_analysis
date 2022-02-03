package com.wangyang.bioinfo.pojo.entity.base;

import com.univocity.parsers.annotations.Parsed;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

/**
 * @author wangyang
 * @date 2021/6/27
 */
//@Entity(name = "t_base_term")
//@DiscriminatorColumn(name = "type", discriminatorType = DiscriminatorType.INTEGER, columnDefinition = "int default 0")
@Data
@MappedSuperclass
public class BaseTerm extends BaseEntity{
//    @Id
//    @GeneratedValue(strategy = GenerationType.AUTO, generator = "myid")
//    @GenericGenerator(name = "myid", strategy = "com.wangyang.bioinfo.support.ManualInsertGenerator")
//    @Parsed
//    private int id;
    @Parsed
    private String name;
    @Parsed
    private String enName;
    private int userId;
    @Column(columnDefinition = "longtext")
    private String description;
}
