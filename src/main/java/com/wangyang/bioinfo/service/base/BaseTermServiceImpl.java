package com.wangyang.bioinfo.service.base;

import com.wangyang.bioinfo.pojo.base.BaseTerm;
import com.wangyang.bioinfo.pojo.param.BaseTermParam;
import com.wangyang.bioinfo.repository.base.BaseTermRepository;
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
 * @date 2021/6/27
 */
public class BaseTermServiceImpl<TERM extends BaseTerm>
        extends AbstractCrudService<TERM,Integer>
        implements IBaseTermService<TERM> {

    @Autowired
    BaseTermRepository<TERM> baseTermRepository;

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
}
