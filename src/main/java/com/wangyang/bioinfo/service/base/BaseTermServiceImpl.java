package com.wangyang.bioinfo.service.base;

import com.wangyang.bioinfo.pojo.entity.base.BaseTerm;
import com.wangyang.bioinfo.pojo.param.BaseTermParam;
import com.wangyang.bioinfo.repository.base.BaseTermRepository;
import com.wangyang.bioinfo.util.BioinfoException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

/**
 * @author wangyang
 * @date 2021/6/27
 */
public class BaseTermServiceImpl<TERM extends BaseTerm>
        extends AbstractCrudService<TERM,Integer>
        implements IBaseTermService<TERM> {

    private final BaseTermRepository<TERM> baseTermRepository;

    public BaseTermServiceImpl(BaseTermRepository<TERM> baseTermRepository) {
        super(baseTermRepository);
        this.baseTermRepository=baseTermRepository;
    }




    @Override
    public Page<TERM> pageBy(BaseTermParam baseTermParam, Pageable pageable) {
        Page<TERM> page = baseTermRepository.findAll(buildSpecByQuery(baseTermParam),pageable);
        return page;
    }

    private Specification<TERM> buildSpecByQuery(BaseTermParam baseTermParam) {
        return (Specification<TERM>) (root, query, criteriaBuilder) ->{
            List<Predicate> predicates = new LinkedList<>();
            if(baseTermParam.getName()!=null){
                predicates.add(criteriaBuilder.equal(root.get("name"),baseTermParam.getName()));
            }
            if(baseTermParam.getEnName()!=null){
                predicates.add(criteriaBuilder.equal(root.get("enName"),baseTermParam.getEnName()));
            }
            if(baseTermParam.getKeyword()!=null){
                String likeCondition = String
                        .format("%%%s%%", StringUtils.strip(baseTermParam.getKeyword()));
                Predicate name = criteriaBuilder.like(root.get("name"), likeCondition);
                Predicate enName = criteriaBuilder.like(root.get("enName"), likeCondition);

                predicates.add(criteriaBuilder.or(name, enName));
            }

            return query.where(predicates.toArray(new Predicate[0])).getRestriction();
        };
    }

    @Override
    @Cacheable(cacheNames = {"TERM"})
    public TERM findByEnName(String name) {
        if(name==null){
            return null;
        }
        List<TERM> terms = baseTermRepository.findAll(new Specification<TERM>() {
            @Override
            public Predicate toPredicate(Root<TERM> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                return criteriaQuery.where(criteriaBuilder.equal(root.get("enName"),name)).getRestriction();
            }
        });
        if(terms.size()==0){
            return null;
        }
        return terms.get(0);
    }
    @Override
    public TERM findById(Integer id){
        if(id==null){
            return null;
        }
        Optional<TERM> fileOptional = baseTermRepository.findById(id);
        return fileOptional.isPresent()?fileOptional.get():null;
    }

    @Override
    public TERM findAndCheckById(Integer id) {
        TERM term = findById(id);
        if(term==null){
            throw new BioinfoException("TERM中要查找的["+term.getName()+"]对象不存在！");
        }
        return term;
    }


    @Override
    @Cacheable(cacheNames="TERM")
    public TERM findAndCheckByEnName(String name) {
        if(name==null|| name.equals("")){
            return null;
        }
        TERM term = findByEnName(name);
        if(term==null){
            throw new BioinfoException("TERM中要查找的["+name+"]对象不存在！");
        }
        return term;
    }

    @Override
    public TERM save(BaseTermParam baseTermParam){
        TERM term = findByEnName(baseTermParam.getEnName());
        if(term==null){
            term = getInstance();
        }
        BeanUtils.copyProperties(baseTermParam,term);
        return save(term);
    }

    @Override
    public List<TERM> findAllById(Collection<Integer> id) {
        List<TERM> terms = baseTermRepository.findAllById(id);
        return terms;
    }

}
