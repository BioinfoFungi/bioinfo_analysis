package com.wangyang.bioinfo.service.impl;

import com.wangyang.bioinfo.pojo.authorize.*;
import com.wangyang.bioinfo.service.*;
import com.wangyang.bioinfo.util.BeanUtil;
import com.wangyang.bioinfo.util.ServiceUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class PermissionServiceImpl implements IPermissionService {
    @Autowired
    IResourceService resourceService;
    @Autowired
    IRoleResourceService roleResourceService;
    @Autowired
    IRoleService roleService;
    @Autowired
    IAPIUserService apiUserService;
    @Autowired
    IUserRoleService userRoleService;

    /**
     * 根据uri查找所需要的角色
     * @param uri
     * @return
     */
    @Override
    public Set<Role> findRolesByResource(String uri){
        /**
         * 查数据库的进行缓存
         */
        List<Resource> resources=  resourceService.listAll();
        Map<String, Resource> resourceMap = ServiceUtil.convertToMap(resources, Resource::getUrl);
        List<Role> roles = roleService.listAll();
        List<RoleResource>  roleResources = roleResourceService.listAll();

        Set<Role> needRoles;
        if(!resourceMap.containsKey(uri)){
            needRoles = new HashSet<>();
            needRoles.add(new Role("anonymous"));
            return needRoles;
        }
        Resource resource = resourceMap.get(uri);
        roleResources = roleResources.stream()
                .filter(roleResource -> roleResource.getResourceId().equals(resource.getId()))
                .collect(Collectors.toList());
        Set<Integer> roleIds = ServiceUtil.fetchProperty(roleResources, RoleResource::getRoleId);
        needRoles = roles.stream()
                .filter(role -> roleIds.contains(role.getId()))
                .collect(Collectors.toSet());
        return needRoles;
    }


    @Override
    public ApiUserDetailDTO findSDKRolesByResource(String authorize){
        APIUser apiUser = apiUserService.findByAuthorize(authorize);
        List<Role> roles = roleService.listAll();
        List<UserRole> userRoles = userRoleService.listAll();
        ApiUserDetailDTO apiUserDetailDTO = new ApiUserDetailDTO();
        Set<Role> needRoles;
        if(apiUser==null){
            needRoles = new HashSet<>();
            needRoles.add(new Role("anonymous"));
            apiUserDetailDTO.setRoles(needRoles);
            return apiUserDetailDTO;
        }
        userRoles = userRoles.stream()
                .filter(userRole -> userRole.getUserId().equals(apiUser.getId()))
                .collect(Collectors.toList());
        Set<Integer> roleIds = ServiceUtil.fetchProperty(userRoles, UserRole::getRoleId);
        needRoles = roles.stream()
                .filter(role -> roleIds.contains(role.getId()))
                .collect(Collectors.toSet());
        BeanUtils.copyProperties(apiUser,apiUserDetailDTO);
        apiUserDetailDTO.setRoles(needRoles);
        return apiUserDetailDTO;
    }

}
