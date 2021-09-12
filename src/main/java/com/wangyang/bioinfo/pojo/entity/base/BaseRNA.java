package com.wangyang.bioinfo.pojo.entity.base;

import com.univocity.parsers.annotations.Parsed;
import lombok.Data;

import javax.persistence.*;

/**
 * @author wangyang
 * @date 2021/6/27
 */
@Entity(name = "t_RNA")
@DiscriminatorColumn(name = "type", discriminatorType = DiscriminatorType.INTEGER, columnDefinition = "int default 0")
//@MappedSuperclass
@Data
public class BaseRNA extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Parsed(field = "gene_id")
    private String geneId;
    @Parsed(field = "gene_name")
    private String name;
    private String alias;
    @Parsed(field = "gene_type")
    private String geneType;
    private String description;
}
