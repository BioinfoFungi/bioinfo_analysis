package com.wangyang.bioinfo.pojo.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
//import org.springframework.data.mongodb.core.mapping.Document;

/**
 * @author wangyang
 * @date 2021/4/24
 * 'data.frame':	41065 obs. of  6 variables:
 *  $ baseMean      : num  0.565 3600.495 3.318 15.659 911.43 ...
 *  $ log2FoldChange: num  1.826 6.174 -0.703 -1.031 -1.247 ...
 *  $ lfcSE         : num  1.478 0.499 0.175 0.157 0.174 ...
 *  $ stat          : num  1.24 12.36 -4.02 -6.58 -7.17 ...
 *  $ pvalue        : num  2.17e-01 4.16e-35 5.77e-05 4.61e-11 7.43e-13 ...
 *  $ padj          : num  2.70e-01 1.10e-33 1.45e-04 2.33e-10 4.38e-12 ...
 */
//@Data
//@Builder
//@Document("DEG")
public class DEG {
    @Id
    public String id;
    public String baseMean;
    public String logFC;
    public String lfcSE;
    public String stat;
    public String pValue;
    public String pAdjustValue;
}
