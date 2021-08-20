package com.wangyang.bioinfo.pojo.file;

import com.univocity.parsers.annotations.Parsed;
import com.wangyang.bioinfo.pojo.base.BaseFile;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

/**
 * 具体的癌症研究
 * @author wangyang
 * @date 2021/6/26
 */

@Data
@DiscriminatorValue(value = "0")
@Entity
public class CancerStudy extends BaseDataCategory {

    @Column(columnDefinition = "longtext")
    private String description;
//    private String processedAbsolutePath;
//    private String processedRelativePath;
    private Integer parentId;
//    private Boolean isProcessed;

}
