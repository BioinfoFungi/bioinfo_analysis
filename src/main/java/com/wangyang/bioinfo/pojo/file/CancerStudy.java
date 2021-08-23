package com.wangyang.bioinfo.pojo.file;

import com.univocity.parsers.annotations.Parsed;
import lombok.Data;

import javax.persistence.*;

/**
 * 具体的癌症研究
 * @author wangyang
 * @date 2021/6/26
 */

@Data
@DiscriminatorValue(value = "0")
@Entity
public class CancerStudy extends TermMapping {
    @Parsed
    private String gse;
    @Column(columnDefinition = "longtext")
    private String description;
//    private String processedAbsolutePath;
//    private String processedRelativePath;
    private Integer parentId=-1;
    private Integer codeId;
//    private Boolean isProcessed;

}
