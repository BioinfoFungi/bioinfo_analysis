package com.wangyang.bioinfo.pojo.entity;


import com.wangyang.bioinfo.pojo.entity.base.BaseFile;
import lombok.Data;

import javax.persistence.Entity;

@Data
@Entity(name = "t_annotation")
public class Annotation extends BaseFile {
    private String enName;
    private Integer codeId;
    private String probeId;
    private String symbol;
    private Integer parentId;
}
