package com.wangyang.bioinfo.pojo.file;

import com.univocity.parsers.annotations.Parsed;
import com.wangyang.bioinfo.pojo.annotation.QueryField;
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
    private Integer codeId;
//    private Boolean isProcessed;
    private Integer parentId=-1;
    @Parsed
    private Integer annotationId;
    @Parsed
    private String metadata;
    private String metadataMd5;
    private Long metadataSize;
    @Parsed
    private String expr;
    private String exprMd5;
    private Long exprSize;
    private Boolean metadataStatus=false;
    private Boolean exprStatus=false;
    private String metadataRelative;
    private String exprRelative;
}
