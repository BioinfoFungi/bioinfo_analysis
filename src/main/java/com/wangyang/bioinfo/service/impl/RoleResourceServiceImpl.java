package com.wangyang.bioinfo.service.impl;

import com.wangyang.bioinfo.pojo.authorize.RoleResource;
import com.wangyang.bioinfo.pojo.authorize.UserRole;
import com.wangyang.bioinfo.repository.RoleResourceRepository;
import com.wangyang.bioinfo.repository.base.BaseRepository;
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
import java.util.stream.Collectors;


@Service
public class RoleResourceServiceImpl extends AbstractCrudService<RoleResource,Integer>
        implements IRoleResourceService {



    private  final RoleResourceRepository resourceRepository;
    public RoleResourceServiceImpl(RoleResourceRepository resourceRepository) {
        super(resourceRepository);
        this.resourceRepository =resourceRepository;
    }

    @Override
    public List<RoleResource> listAll() {
        return resourceRepository.listAllCached();
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
        List<RoleResource> roleResources = listAll().stream()
                .filter(roleResource -> roleResource.getResourceId().equals(resourceId) && roleResource.getRoleId().equals(roleId))
                .collect(Collectors.toList());
        return roleResources.size()==0?null:roleResources.get(0);
    }

    @Override
    public List<RoleResource> findByRoleId(int roleId){
        List<RoleResource> roleResources = listAll().stream()
                .filter(roleResource -> roleResource.getRoleId().equals(roleId))
                .collect(Collectors.toList());
        return roleResources;
    }

    @Override
    public List<RoleResource> findByResourceId(int resourceId){
        List<RoleResource> roleResources = listAll().stream()
                .filter(roleResource -> roleResource.getResourceId().equals(resourceId))
                .collect(Collectors.toList());
        return roleResources;
    }
}
