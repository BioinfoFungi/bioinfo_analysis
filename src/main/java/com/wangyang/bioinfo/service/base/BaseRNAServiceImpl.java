package com.wangyang.bioinfo.service.base;

import com.wangyang.bioinfo.repository.base.BaseRepository;
import com.wangyang.bioinfo.txt.IDataInputService;
import com.wangyang.bioinfo.pojo.base.BaseRNA;
import com.wangyang.bioinfo.pojo.param.BaseRNAQuery;
import com.wangyang.bioinfo.pojo.vo.RNAVO;
import com.wangyang.bioinfo.repository.base.BaseRNARepository;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.LinkedList;
import java.util.List;


/**
 * @author wangyang
 * @date 2021/6/27
 */

public class BaseRNAServiceImpl<T extends BaseRNA> extends AbstractCrudService<T,Integer> implements IBaseRNAService<T> {

    private final BaseRNARepository<T> baseRNARepository;

    public BaseRNAServiceImpl(BaseRNARepository<T> baseRNARepository) {
        super(baseRNARepository);
        this.baseRNARepository=baseRNARepository;
    }


//    public BaseRNAServiceImpl(BaseRNARepository<T> baseRNARepository) {
//        super(baseRNARepository);
//        this.baseRNARepository = baseRNARepository;
//    }


    @Override
    public T add(T ti) {
        T t = findByGeneId(ti.getGeneId());
        if(t==null){
            t = getInstance();
        }
        BeanUtils.copyProperties(ti,t,"id");
        return super.add(t);
    }


    @Override
    public T findByGeneId(String geneId){
        List<T> tList = baseRNARepository.findAll(new Specification<T>() {
            @Override
            public Predicate toPredicate(Root<T> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                return criteriaQuery.where(criteriaBuilder.equal(root.get("geneId"),geneId)).getRestriction();
            }
        });
        if(tList.size()==0){
            return null;
        }
        return tList.get(0);
    }
    @Override
    public T findByName(String name){
        List<T> tList = baseRNARepository.findAll(new Specification<T>() {
            @Override
            public Predicate toPredicate(Root<T> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                return criteriaQuery.where(criteriaBuilder.equal(root.get("name"),name)).getRestriction();
            }
        });
        if(tList.size()==0){
            return null;
        }
        return tList.get(0);
    }

    private Specification<T> buildSpecByQuery(BaseRNAQuery baseRNAQuery){
        return (Specification<T>) (root, query, criteriaBuilder) ->{
            List<Predicate> predicates = new LinkedList<>();
            if(baseRNAQuery.getName()!=null){
                predicates.add(criteriaBuilder.equal(root.get("name"), baseRNAQuery.getName()));
            }
            if(baseRNAQuery.getDescription()!=null){
                predicates.add(criteriaBuilder.equal(root.get("description"), baseRNAQuery.getDescription()));
            }
            if(baseRNAQuery.getKeyword()!=null){
                String likeCondition = String
                        .format("%%%s%%", StringUtils.strip(baseRNAQuery.getKeyword()));

                // Build like predicate
                Predicate geneId = criteriaBuilder.like(root.get("geneId"), likeCondition);
                Predicate alias = criteriaBuilder.like(root.get("alias"), likeCondition);
                Predicate name = criteriaBuilder.like(root.get("name"), likeCondition);
                Predicate description = criteriaBuilder
                        .like(root.get("description"), likeCondition);

                predicates.add(criteriaBuilder.or(name, description,geneId,alias));
            }
            return query.where(predicates.toArray(new Predicate[0])).getRestriction();
        };
    }
    @Override
    public Page<T> pageBy(BaseRNAQuery baseRNAQuery, Pageable pageable) {
        Page<T> page = baseRNARepository.findAll(buildSpecByQuery(baseRNAQuery),pageable);
        return page;
    }

    @Override
    public Page<RNAVO> convert(Page<T> pages) {
        Page<RNAVO> rnavos = pages.map(rna -> {
            RNAVO rnavo = new RNAVO();
            BeanUtils.copyProperties(rna,rnavo);
            return rnavo;
        });
        return rnavos;
    }

    //    private T getInstanceOfT()
//    {
//        ParameterizedType superClass = (ParameterizedType) getClass().getGenericSuperclass();
//        Class<T> type = (Class<T>) superClass.getActualTypeArguments()[0];
//        try
//        {
//            return type.newInstance();
//        }
//        catch (Exception e)
//        {
//            // Oops, no default constructor
//            throw new RuntimeException(e);
//        }
//    }

}
