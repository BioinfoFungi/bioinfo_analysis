package com.wangyang.bioinfo.pojo.base;

import com.univocity.parsers.annotations.Parsed;
import lombok.Data;

import javax.persistence.*;

/**
 * @author wangyang
 * @date 2021/6/27
 */
@Entity(name = "t_base_term")
@DiscriminatorColumn(name = "type", discriminatorType = DiscriminatorType.INTEGER, columnDefinition = "int default 0")
@Data
public class BaseTerm extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Parsed
    private int id;
    @Parsed
    private String name;
    @Parsed
    private String enName;
    private int userId;
    @Column(columnDefinition = "longtext")
    private String description;
}
