package com.wangyang.bioinfo.service.impl;

import com.wangyang.bioinfo.pojo.authorize.Resource;
import com.wangyang.bioinfo.pojo.authorize.Role;
import com.wangyang.bioinfo.pojo.authorize.RoleResource;
import com.wangyang.bioinfo.repository.ResourceRepository;
import com.wangyang.bioinfo.service.IResourceService;
import com.wangyang.bioinfo.service.IRoleResourceService;
import com.wangyang.bioinfo.service.base.AbstractCrudService;
import com.wangyang.bioinfo.util.ServiceUtil;
import org.hibernate.annotations.Cache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author wangyang
 * @date 2021/5/5
 */
@Service
public class ResourceServiceImpl extends AbstractCrudService<Resource,Integer>
        implements IResourceService {

    private final IRoleResourceService roleResourceService;
    private final  ResourceRepository resourceRepository;
    public ResourceServiceImpl(ResourceRepository resourceRepository,
                               IRoleResourceService roleResourceService) {
        super(resourceRepository);
        this.resourceRepository = resourceRepository;
        this.roleResourceService=roleResourceService;
    }

    @Override
    public List<Resource> listAll() {
        return resourceRepository.listAllCached();
    }

    @Override
    public Resource addResource(Resource resource) {
        return null;
    }



    @Override
    public Resource findRoleById(int id) {
        return null;
    }

    @Override
    public Resource delResource(int id) {
        return null;
    }

    @Override
    public Page<Resource> pageResource(Pageable pageable) {
        return null;
    }

    @Override
    public Resource listByUri(String Uri) {
        return null;
    }

    public List<Resource> findByIds(Iterable<Integer> inputIds){
        Set<Integer> ids = (Set<Integer> )inputIds;
        List<Resource> resourceList = listAll().stream()
                .filter(resource -> ids.contains(resource.getId()))
                .collect(Collectors.toList());
        return resourceList;
    }

    @Override
    public List<Resource> findByRoleId(Integer id) {
        List<RoleResource> roleResources = roleResourceService.findByRoleId(id);
        Set<Integer> resourceIds = ServiceUtil.fetchProperty(roleResources, RoleResource::getResourceId);
        List<Resource> resources = findByIds(resourceIds);
        return resources;
    }
}
