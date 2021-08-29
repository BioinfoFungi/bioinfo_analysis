package com.wangyang.bioinfo.service;

import com.wangyang.bioinfo.pojo.Task;
import com.wangyang.bioinfo.pojo.authorize.Resource;
import com.wangyang.bioinfo.pojo.authorize.RoleResource;
import com.wangyang.bioinfo.service.base.ICrudService;

import java.util.List;
import java.util.Set;

public interface IRoleResourceService  extends ICrudService<RoleResource, Integer> {

    RoleResource findBy(Integer resourceId, Integer roleId);

    List<RoleResource> findByRoleId(int roleId);

    List<RoleResource> findByResourceId(int resourceId);

    List<RoleResource> findByResourceId(Set<Integer> ids);

    void init();

//    List<RoleResourceDTO> findDTOByRoleId(int roleId);
//
//    List<RoleResourceDTO> findDTOByResourceId(int resourceId);
//
//    List<Resource> findResourceWithoutRoleId(int roleId);
}
