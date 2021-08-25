package com.wangyang.bioinfo.service.impl;

import com.wangyang.bioinfo.pojo.authorize.RoleResource;
import com.wangyang.bioinfo.service.IRoleResourceService;
import com.wangyang.bioinfo.service.base.AbstractCrudService;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class RoleResourceServiceImpl extends AbstractCrudService<RoleResource,Integer>
        implements IRoleResourceService {

    @Override
    @Cacheable(cacheNames = {"AUTHORIZE"})
    public List<RoleResource> listAll() {
        return super.listAll();
    }
}
