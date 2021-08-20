package com.wangyang.bioinfo.service.base;

import com.wangyang.bioinfo.pojo.file.BaseDataCategory;
import com.wangyang.bioinfo.pojo.file.CancerStudy;
import com.wangyang.bioinfo.repository.base.BaseDataCategoryRepository;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.Predicate;
import java.util.LinkedList;
import java.util.List;

/**
 * @author wangyang
 * @date 2021/7/25
 */
public class BaseDataCategoryServiceImpl<CATEGORY extends BaseDataCategory>
        extends BaseFileService<CATEGORY>
        implements IBaseDataCategoryService<CATEGORY>{

    @Autowired
    BaseDataCategoryRepository<CATEGORY> baseDataCategoryRepository;


    public Specification<CATEGORY> buildSpecByDataCategory(BaseDataCategory cancerStudy,String keywords) {
        return (Specification<CATEGORY>) (root, query, criteriaBuilder) ->{
            List<Predicate> predicates = new LinkedList<>();
            if(cancerStudy.getAnalysisSoftwareId()!=null){
                predicates.add(criteriaBuilder.equal(root.get("analysisSoftwareId"),cancerStudy.getAnalysisSoftwareId()));
            }
            if(cancerStudy.getDataCategoryId()!=null){
                predicates.add(criteriaBuilder.equal(root.get("dataCategoryId"),cancerStudy.getDataCategoryId()));
            }
            if(cancerStudy.getCancerId()!=null){
                predicates.add(criteriaBuilder.equal(root.get("cancerId"),cancerStudy.getCancerId()));
            }
            if(cancerStudy.getDataOriginId()!=null){
                predicates.add(criteriaBuilder.equal(root.get("dataOriginId"),cancerStudy.getDataOriginId()));
            }
            if(cancerStudy.getStudyId()!=null){
                predicates.add(criteriaBuilder.equal(root.get("studyId"),cancerStudy.getStudyId()));
            }
            if(cancerStudy.getFileName()!=null){
                predicates.add(criteriaBuilder.equal(root.get("fileName"),cancerStudy.getFileName()));
            }
            if(keywords!=null){
                String likeCondition = String
                        .format("%%%s%%", StringUtils.strip(keywords));
                Predicate fileName = criteriaBuilder.like(root.get("fileName"), likeCondition);

                predicates.add(criteriaBuilder.or(fileName));
            }
            if(cancerStudy.getUuid()!=null){
                predicates.add(criteriaBuilder.equal(root.get("uuid"),cancerStudy.getUuid()));
            }
            if(cancerStudy.getGse()!=null){
                predicates.add(criteriaBuilder.equal(root.get("gse"),cancerStudy.getGse()));
            }
            return query.where(criteriaBuilder.and(predicates.toArray(new Predicate[0]))).getRestriction();
        };
    }

    @Override
    public Page<CATEGORY> pageBy(CancerStudy cancerStudyParam,String keyWards, Pageable pageable) {
        Page<CATEGORY> categories = baseDataCategoryRepository.findAll(buildSpecByDataCategory(cancerStudyParam,keyWards), pageable);
        return categories;
    }

    @Override
    public List<CATEGORY> findDataByCategoryId(CancerStudy cancerStudy,String keyWards) {
        List<CATEGORY> categories = baseDataCategoryRepository.findAll(buildSpecByDataCategory(cancerStudy,keyWards));
        return categories;
    }
}
