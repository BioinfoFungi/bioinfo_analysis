package com.wangyang.bioinfo.service;

import com.wangyang.bioinfo.pojo.authorize.UserRole;
import com.wangyang.bioinfo.service.base.ICrudService;

import java.util.List;

public interface IUserRoleService  extends ICrudService<UserRole, Integer> {

    UserRole findBy(Integer userId, Integer roleId);

    List<UserRole> findByUserId(Integer id);

    List<UserRole> findByRoleId(Integer roleId);
}
