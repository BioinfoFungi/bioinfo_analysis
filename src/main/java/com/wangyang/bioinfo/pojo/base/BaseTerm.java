package com.wangyang.bioinfo.pojo.base;

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
    private int id;

    private String name;
    private String enName;
    private int userId;
    @Column(columnDefinition = "longtext not null")
    private String description;
}
