package com.wangyang.bioinfo.service.impl;

import com.wangyang.bioinfo.pojo.authorize.UserRole;
import com.wangyang.bioinfo.service.IUserRoleService;
import com.wangyang.bioinfo.service.base.AbstractCrudService;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserRoleServiceImpl extends AbstractCrudService<UserRole,Integer>
        implements IUserRoleService {

    @Override
    @Cacheable(cacheNames = {"AUTHORIZE"})
    public List<UserRole> listAll() {
        return super.listAll();
    }
}
