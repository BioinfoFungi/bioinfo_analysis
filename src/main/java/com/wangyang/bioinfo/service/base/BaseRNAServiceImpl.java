package com.wangyang.bioinfo.service.base;

import com.wangyang.bioinfo.pojo.BaseRNA;
import com.wangyang.bioinfo.pojo.CancerStudy;
import com.wangyang.bioinfo.pojo.param.BaseRNAParam;
import com.wangyang.bioinfo.pojo.param.CancerStudyQuery;
import com.wangyang.bioinfo.repository.base.BaseRNARepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.lang.reflect.ParameterizedType;
import java.util.LinkedList;
import java.util.List;


/**
 * @author wangyang
 * @date 2021/6/27
 */

public class BaseRNAServiceImpl<T extends BaseRNA> extends AbstractCrudService<T,Integer> implements IBaseRNAService<T> {

    @Autowired
    BaseRNARepository<T> baseRNARepository;

//    public BaseRNAServiceImpl(BaseRNARepository<T> baseRNARepository) {
//        super(baseRNARepository);
//        this.baseRNARepository = baseRNARepository;
//    }


    @Override
    public T add(T ti) {
        T t = findByName(ti.getName());
        if(t==null){
            t = getInstanceOfT();
        }
        BeanUtils.copyProperties(ti,t,"id");
        return super.add(t);
    }

    @Override
    public T findByName(String name){
        List<T> tList = baseRNARepository.findAll(new Specification<T>() {
            @Override
            public Predicate toPredicate(Root<T> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                return criteriaQuery.where(criteriaBuilder.equal(root.get("name"),name)).getRestriction();
            }
        });
        if(tList==null){
            return null;
        }
        return tList.get(0);
    }

    private Specification<T> buildSpecByQuery(BaseRNAParam baseRNAParam){
        return (Specification<T>) (root, query, criteriaBuilder) ->{
            List<Predicate> predicates = new LinkedList<>();
            if(baseRNAParam.getName()!=null){
                criteriaBuilder.equal(root.get("name"),baseRNAParam.getName());
            }
            return query.where(predicates.toArray(new Predicate[0])).getRestriction();
        };
    }
    @Override
    public Page<T> pageBy(BaseRNAParam baseRNAParam, Pageable pageable) {
        Page<T> page = baseRNARepository.findAll(buildSpecByQuery(baseRNAParam),pageable);
        return page;
    }

    private T getInstanceOfT()
    {
        ParameterizedType superClass = (ParameterizedType) getClass().getGenericSuperclass();
        Class<T> type = (Class<T>) superClass.getActualTypeArguments()[0];
        try
        {
            return type.newInstance();
        }
        catch (Exception e)
        {
            // Oops, no default constructor
            throw new RuntimeException(e);
        }
    }

}
