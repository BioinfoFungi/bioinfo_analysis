package com.wangyang.bioinfo.service.impl;

import com.wangyang.bioinfo.pojo.authorize.APIUser;
import com.wangyang.bioinfo.pojo.enums.CrudType;
import com.wangyang.bioinfo.repository.ApiUserRepository;
import com.wangyang.bioinfo.service.IAPIUserService;
import com.wangyang.bioinfo.service.base.AbstractAuthorizeServiceImpl;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
public class APIUserServiceImpl extends AbstractAuthorizeServiceImpl<APIUser>
        implements IAPIUserService{


    private  final ApiUserRepository apiUserRepository;

    public APIUserServiceImpl(ApiUserRepository apiUserRepository) {
        super(apiUserRepository);
        this.apiUserRepository=apiUserRepository;
    }

    @Override
    public APIUser findByAuthorize(String authorize) {
        List<APIUser> apiUserList = apiUserRepository.findAll(new Specification<APIUser>() {
            @Override
            public Predicate toPredicate(Root<APIUser> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                return criteriaQuery.where(criteriaBuilder.equal(root.get("authorize"),authorize)).getRestriction();
            }
        });
        return apiUserList.size()==0?null:apiUserList.get(0);
    }

    @Override
    public boolean supportType(CrudType type) {
        return false;
    }
}
