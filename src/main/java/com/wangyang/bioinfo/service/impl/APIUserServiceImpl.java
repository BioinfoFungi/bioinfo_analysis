package com.wangyang.bioinfo.service.impl;

import com.wangyang.bioinfo.pojo.authorize.APIUser;
import com.wangyang.bioinfo.repository.ApiUserRepository;
import com.wangyang.bioinfo.repository.base.BaseAuthorizeRepository;
import com.wangyang.bioinfo.service.IAPIUserService;
import com.wangyang.bioinfo.service.base.BaseAuthorizeServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.List;

@Service
public class APIUserServiceImpl extends BaseAuthorizeServiceImpl<APIUser>
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
}
