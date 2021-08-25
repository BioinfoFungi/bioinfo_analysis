package com.wangyang.bioinfo.service.impl;

import com.wangyang.bioinfo.pojo.authorize.Resource;
import com.wangyang.bioinfo.repository.ResourceRepository;
import com.wangyang.bioinfo.service.IResourceService;
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

/**
 * @author wangyang
 * @date 2021/5/5
 */
@Service
public class ResourceServiceImpl extends AbstractCrudService<Resource,Integer>
        implements IResourceService {

    @Autowired
    ResourceRepository resourceRepository;

    @Override
    public Resource addResource(Resource resource) {
        return null;
    }

    @Override
    @Cacheable(cacheNames = {"AUTHORIZE"})
    public List<Resource> listAll() {
        return super.listAll();
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
    public Map<String, Resource> listAllMap() {
        List<Resource> resources = resourceRepository.findAll();
        Map<String, Resource> resourceMap = ServiceUtil.convertToMap(resources, Resource::getPath);
        return resourceMap;
    }
}
