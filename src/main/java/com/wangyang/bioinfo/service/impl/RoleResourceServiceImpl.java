package com.wangyang.bioinfo.service.impl;

import com.wangyang.bioinfo.pojo.authorize.RoleResource;
import com.wangyang.bioinfo.pojo.authorize.UserRole;
import com.wangyang.bioinfo.repository.RoleResourceRepository;
import com.wangyang.bioinfo.service.IRoleResourceService;
import com.wangyang.bioinfo.service.base.AbstractCrudService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.List;


@Service
public class RoleResourceServiceImpl extends AbstractCrudService<RoleResource,Integer>
        implements IRoleResourceService {


    @Autowired
    RoleResourceRepository resourceRepository;

    @Override
    @Cacheable(cacheNames = {"AUTHORIZE_ROLE_RESOURCE"})
    public List<RoleResource> listAll() {
        return super.listAll();
    }


    @Override
    public RoleResource save(RoleResource roleResource) {
        RoleResource resource = findBy(roleResource.getResourceId(), roleResource.getRoleId());
        if(resource==null){
            resource = super.save(roleResource);
        }
        return resource;
    }

    @Override
    public RoleResource findBy(Integer resourceId, Integer roleId){
        List<RoleResource> userRoles = resourceRepository.findAll(new Specification<RoleResource>() {
            @Override
            public Predicate toPredicate(Root<RoleResource> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                return criteriaQuery.where(
                        criteriaBuilder.equal(root.get("resourceId"),resourceId),
                        criteriaBuilder.equal(root.get("roleId"),roleId)
                ).getRestriction();
            }
        });
        return userRoles.size()==0?null:userRoles.get(0);
    }
}
