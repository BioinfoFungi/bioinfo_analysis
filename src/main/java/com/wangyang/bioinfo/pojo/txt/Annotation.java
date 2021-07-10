package com.wangyang.bioinfo.pojo.txt;

import com.univocity.parsers.annotations.Parsed;
import lombok.Data;

/**
 * @author wangyang
 * @date 2021/7/10
 */
@Data
public class Annotation {

    @Parsed(field = "gene_id")
    private String geneId;
    @Parsed(field = "gene_type")
    private String geneType;
}
