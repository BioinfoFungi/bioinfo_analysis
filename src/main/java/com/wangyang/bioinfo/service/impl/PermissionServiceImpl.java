package com.wangyang.bioinfo.service.impl;

import com.wangyang.bioinfo.pojo.authorize.*;
import com.wangyang.bioinfo.service.*;
import com.wangyang.bioinfo.util.BeanUtil;
import com.wangyang.bioinfo.util.ServiceUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.config.annotation.PathMatchConfigurer;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
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
    private final Pattern pattern = Pattern.compile("\\{.*?\\}");
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
//        Map<String, Resource> resourceMap = resources.stream()
//                .collect(Collectors.toMap(resource ->
//                        resource.getMethod()+resource.getUrl(),resource -> resource));
        List<Role> roles = roleService.listAll();
        List<RoleResource>  roleResources = roleResourceService.listAll();



        Resource findResource=null;
        for (Resource resource : resources){
            String url = resource.getMethod()+resource.getUrl();
            Matcher matcher = pattern.matcher(url);
            if(matcher.find()){
                String regUrl = matcher.replaceAll("(.*?)");
                boolean matches = uri.matches(regUrl);
                if(matches){
                    findResource=resource;
                    break;
                }
            }else if (url.equals(uri)){
                findResource = resource;
                break;
            }
        }
        Set<Role> needRoles = new HashSet<>();
        if(findResource==null){
            needRoles.add(new Role("anonymous"));
            return needRoles;
        }

        Resource finalFindResource = findResource;
        roleResources = roleResources.stream()
                .filter(roleResource -> roleResource.getResourceId().equals(finalFindResource.getId()))
                .collect(Collectors.toList());
        Set<Integer> roleIds = ServiceUtil.fetchProperty(roleResources, RoleResource::getRoleId);
        needRoles = roles.stream()
                .filter(role -> roleIds.contains(role.getId()))
                .collect(Collectors.toSet());

        if(needRoles.size()==0){
            needRoles.add(new Role("anonymous"));
        }
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
