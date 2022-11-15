package com.wangyang.bioinfo.pojo.entity.snakemake;

import com.wangyang.bioinfo.pojo.entity.base.BaseEntity;
import lombok.Data;

import javax.persistence.Entity;

@Data
@Entity(name = "t_snakemake")
public class Snakemake extends BaseEntity {
    private String input;
    private String name;
    private String output;
    private Integer userId;

}
