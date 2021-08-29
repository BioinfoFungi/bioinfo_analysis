package com.wangyang.bioinfo.service.impl;

import com.wangyang.bioinfo.pojo.authorize.Resource;
import com.wangyang.bioinfo.pojo.authorize.RoleResource;
import com.wangyang.bioinfo.pojo.authorize.ResourceVO;
import com.wangyang.bioinfo.repository.ResourceRepository;
import com.wangyang.bioinfo.service.IResourceService;
import com.wangyang.bioinfo.service.IRoleResourceService;
import com.wangyang.bioinfo.service.base.AbstractCrudService;
import com.wangyang.bioinfo.util.ServiceUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.io.Serializable;
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

    private final  ResourceRepository resourceRepository;
    public final IRoleResourceService roleResourceService;
    public ResourceServiceImpl(ResourceRepository resourceRepository,
                               IRoleResourceService roleResourceService) {
        super(resourceRepository);
        this.resourceRepository = resourceRepository;
        this.roleResourceService=roleResourceService;
    }

    @Override
    public List<Resource> listAll() {
        return resourceRepository.findAll();
    }


    @Override
    public Resource save(Resource resource) {
        return resourceRepository.save(resource);
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

    @Override
    public List<Resource> findByIds(Iterable<Integer> inputIds){
        Set<Integer> ids = (Set<Integer> )inputIds;
        List<Resource> resourceList = listAll().stream()
                .filter(resource -> ids.contains(resource.getId()))
                .collect(Collectors.toList());
        return resourceList;
    }
    @Override
    public List<Resource> findByWithoutIds(Iterable<Integer> inputIds){
        Set<Integer> ids = (Set<Integer> )inputIds;
        List<Resource> resourceList = listAll().stream()
                .filter(resource -> !ids.contains(resource.getId()))
                .collect(Collectors.toList());
        return resourceList;
    }
//
    @Override
    public List<ResourceVO> findByRoleId(Integer id) {
        List<RoleResource> roleResources = roleResourceService.findByRoleId(id);
        Set<Integer> resourceIds = ServiceUtil.fetchProperty(roleResources, RoleResource::getResourceId);
        List<Resource> resources = findByIds(resourceIds);
        Map<Integer, Resource> resourceMap = ServiceUtil.convertToMap(resources, Resource::getId);

        List<ResourceVO> resourceVOS = roleResources.stream().map(roleResource -> {
            ResourceVO resourceVO = new ResourceVO();
            Resource resource = resourceMap.get(roleResource.getResourceId());
            BeanUtils.copyProperties(resource,resourceVO);
            resourceVO.setResourceRoleId(roleResource.getId());
            resourceVO.setRoleId(roleResource.getRoleId());
            return resourceVO;
        }).collect(Collectors.toList());
        return resourceVOS;
    }

    @Override
    public List<Resource> findByWithoutRoleId(Integer id) {
        List<RoleResource> roleResources = roleResourceService.findByRoleId(id);
        Set<Integer> resourceIds = ServiceUtil.fetchProperty(roleResources, RoleResource::getResourceId);
        List<Resource> resources = findByWithoutIds(resourceIds);
        return resources;
    }


    @Override
    public void deleteAll(Iterable<Resource> resources) {
        List<Resource> resourceList = (List<Resource>)resources;
        Set<Integer> ids = ServiceUtil.fetchProperty(resourceList, Resource::getId);
        List<RoleResource> roleResources = roleResourceService.findByResourceId(ids);
        roleResourceService.deleteAll(roleResources);
        super.deleteAll(resources);
    }
}
