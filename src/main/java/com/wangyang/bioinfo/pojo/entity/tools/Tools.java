package com.wangyang.bioinfo.pojo.entity.tools;

import com.wangyang.bioinfo.pojo.entity.base.BaseEntity;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;

@Data
@Entity(name = "t_tools")
public class Tools extends BaseEntity {
    private String name;
    private Integer userId;
    private Integer codeId;
    @Column(columnDefinition = "longtext")
    private String formRule;
}
