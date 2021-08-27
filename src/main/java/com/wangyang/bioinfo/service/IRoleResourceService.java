package com.wangyang.bioinfo.service;

import com.wangyang.bioinfo.pojo.Task;
import com.wangyang.bioinfo.pojo.authorize.RoleResource;
import com.wangyang.bioinfo.service.base.ICrudService;

import java.util.List;

public interface IRoleResourceService  extends ICrudService<RoleResource, Integer> {

    RoleResource findBy(Integer resourceId, Integer roleId);

    List<RoleResource> findByRoleId(int roleId);
}