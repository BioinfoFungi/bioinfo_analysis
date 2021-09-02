package com.wangyang.bioinfo.pojo.file;


import com.wangyang.bioinfo.pojo.base.BaseFile;
import io.swagger.models.auth.In;
import lombok.Data;

import javax.persistence.Entity;

@Data
@Entity(name = "t_annotation")
public class Annotation extends BaseFile {
    private String enName;
    private Integer codeId;
}
