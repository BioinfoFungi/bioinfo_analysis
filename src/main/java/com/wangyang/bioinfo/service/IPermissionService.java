package com.wangyang.bioinfo.service;

import com.wangyang.bioinfo.pojo.authorize.ApiUserDetailDTO;
import com.wangyang.bioinfo.pojo.authorize.Role;

import java.util.Set;

public interface IPermissionService {
    Set<Role> findRolesByResource(String uri);

    ApiUserDetailDTO findSDKRolesByResource(String authorize);
}
