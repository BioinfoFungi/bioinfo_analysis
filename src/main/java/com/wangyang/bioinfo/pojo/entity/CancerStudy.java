package com.wangyang.bioinfo.pojo.entity;

import com.univocity.parsers.annotations.Parsed;
import com.wangyang.bioinfo.pojo.annotation.QueryField;
import com.wangyang.bioinfo.pojo.entity.base.TermMapping;
import lombok.Data;

import javax.persistence.*;
import java.util.Objects;

/**
 * 具体的癌症研究
 * @author wangyang
 * @date 2021/6/26
 */

@Data
//@DiscriminatorValue(value = "0")
@Entity(name = "t_cancer_study")
public class CancerStudy extends TermMapping {


    @Parsed
    private String gse;
    @Column(columnDefinition = "longtext")
    private String description;
    private Integer codeId;
    @Column(columnDefinition = "longtext")
    @Parsed
    private String param;
    @QueryField
    private Integer parentId=-1;
    @Parsed
    private String annotation;
    private Boolean codeIsSuccess=false;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CancerStudy)) return false;
        if (!super.equals(o)) return false;
        CancerStudy that = (CancerStudy) o;
        return Objects.equals(getGse(), that.getGse()) &&
                Objects.equals(getCancerId(), that.getCancerId()) &&
                Objects.equals(getDataOriginId(), that.getDataOriginId()) &&
                Objects.equals(getDataCategoryId(), that.getDataCategoryId()) &&
                Objects.equals(getStudyId(), that.getStudyId()) &&
                Objects.equals(getAnalysisSoftwareId(), that.getAnalysisSoftwareId()) ;
    }

    @Override
    public int hashCode() {
//        super.hashCode(),
        return Objects.hash( getGse(), getCancerId(),getDataOriginId(),getDataCategoryId(),getStudyId(),getAnalysisSoftwareId());
    }


    //    @Parsed
//    private String metadata;
//    private String metadataMd5;
//    private Long metadataSize;
//    @Parsed
//    private String expr;
//    private String exprMd5;
//    private Long exprSize;
//    private Boolean metadataStatus=false;
//    private Boolean exprStatus=false;
//    private String metadataRelative;
//    private String exprRelative;


}
