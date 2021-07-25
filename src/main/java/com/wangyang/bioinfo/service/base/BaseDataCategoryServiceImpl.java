package com.wangyang.bioinfo.service.base;

import com.wangyang.bioinfo.pojo.dto.DataCategoryIdDto;
import com.wangyang.bioinfo.pojo.file.BaseDataCategory;
import com.wangyang.bioinfo.pojo.file.CancerStudy;
import com.wangyang.bioinfo.pojo.param.BaseTermParam;
import com.wangyang.bioinfo.repository.base.BaseDataCategoryRepository;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
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


    private Specification<CATEGORY> buildSpecByDataCategory(DataCategoryIdDto categoryId) {
        return (Specification<CATEGORY>) (root, query, criteriaBuilder) ->{
            List<Predicate> predicates = new LinkedList<>();
            if(categoryId.getAnalysisSoftwareId()!=null){
                predicates.add(criteriaBuilder.equal(root.get("analysisSoftwareId"),categoryId.getAnalysisSoftwareId()));
            }
            if(categoryId.getExperimentalStrategyId()!=null){
                predicates.add(criteriaBuilder.equal(root.get("experimentalStrategyId"),categoryId.getExperimentalStrategyId()));
            }
            if(categoryId.getCancerId()!=null){
                predicates.add(criteriaBuilder.equal(root.get("cancerId"),categoryId.getCancerId()));
            }
            if(categoryId.getDataOriginId()!=null){
                predicates.add(criteriaBuilder.equal(root.get("dataOriginId"),categoryId.getDataOriginId()));
            }
            if(categoryId.getStudyId()!=null){
                predicates.add(criteriaBuilder.equal(root.get("studyId"),categoryId.getStudyId()));
            }
            return query.where(criteriaBuilder.and(predicates.toArray(new Predicate[0]))).getRestriction();
        };
    }

    @Override
    public List<CATEGORY> findDataByCategoryId(DataCategoryIdDto dataCategoryId) {
        List<CATEGORY> categories = baseDataCategoryRepository.findAll(buildSpecByDataCategory(dataCategoryId));
        return categories;
    }
}
