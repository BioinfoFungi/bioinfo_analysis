package com.wangyang.bioinfo.service.impl;

import com.wangyang.bioinfo.pojo.annotation.Anonymous;
import com.wangyang.bioinfo.pojo.authorize.Resource;
import com.wangyang.bioinfo.pojo.authorize.Role;
import com.wangyang.bioinfo.pojo.authorize.RoleResource;
import com.wangyang.bioinfo.pojo.dto.RoleUrl;
import com.wangyang.bioinfo.repository.ResourceRepository;
import com.wangyang.bioinfo.repository.RoleRepository;
import com.wangyang.bioinfo.repository.RoleResourceRepository;
import com.wangyang.bioinfo.service.IRoleResourceService;
import com.wangyang.bioinfo.service.base.AbstractCrudService;
import com.wangyang.bioinfo.util.ServiceUtil;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;


@Service
public class RoleResourceServiceImpl extends AbstractCrudService<RoleResource,Integer>
        implements IRoleResourceService {



    private  final RoleResourceRepository roleResourceRepository;
    private final ApplicationContext applicationContext;
    private final RoleRepository roleRepository;
    private final ResourceRepository resourceRepository;
//    private final  IRoleService roleService;
//    private final IResourceService resourceService;
    public RoleResourceServiceImpl(RoleResourceRepository roleResourceRepository,
                                   ApplicationContext applicationContext,
                                   RoleRepository roleRepository,
                                   ResourceRepository resourceRepository){
        super(roleResourceRepository);
        this.roleResourceRepository =roleResourceRepository;
        this.applicationContext=applicationContext;
        this.roleRepository = roleRepository;
        this.resourceRepository = resourceRepository;
//        this.roleService =roleService;
//        this.resourceService = resourceService;

    }

    @Override
    public List<RoleResource> listAll() {
        return roleResourceRepository.findAll();
    }

    @Override
    public RoleResource save(RoleResource roleResource) {
        RoleResource resource = findBy(roleResource.getResourceId(), roleResource.getRoleId());
        if(resource==null){
            resource = super.save(roleResource);
            return resource;
        }
        return null;
    }

    @Override
    public RoleResource findBy(Integer resourceId, Integer roleId){
        List<RoleResource> roleResources = listAll().stream()
                .filter(roleResource -> roleResource.getResourceId().equals(resourceId) && roleResource.getRoleId().equals(roleId))
                .collect(Collectors.toList());
        return roleResources.size()==0?null:roleResources.get(0);
    }

    @Override
    public List<RoleResource> findByRoleId(int roleId){
        List<RoleResource> roleResources = listAll().stream()
                .filter(roleResource -> roleResource.getRoleId().equals(roleId))
                .collect(Collectors.toList());
        return roleResources;
    }

    @Override
    public List<RoleResource> findByResourceId(int resourceId){
        List<RoleResource> roleResources = listAll().stream()
                .filter(roleResource -> roleResource.getResourceId().equals(resourceId))
                .collect(Collectors.toList());
        return roleResources;
    }

    @Override
    public List<RoleResource> findByResourceId(Set<Integer> ids) {
        List<RoleResource> roleResources = listAll().stream()
                .filter(roleResource -> ids.contains(roleResource.getResourceId()))
                .collect(Collectors.toList());
        return roleResources;
    }

    @Override
    public void init(){
//        RequestMappingHandlerMapping mapping = applicationContext.getBean("requestMappingHandlerMapping",RequestMappingHandlerMapping.class);
//        Map<RequestMappingInfo, HandlerMethod> methodMap = mapping.getHandlerMethods();
//        List<RoleUrl> roleResourceName = new ArrayList<>();
//        for (RequestMappingInfo info : methodMap.keySet()){
//            HandlerMethod handlerMethod = methodMap.get(info);
//            Set<String> urlSet = info.getPatternsCondition().getPatterns();
//            String url = urlSet.iterator().next();
//            Set<RequestMethod> methodSet = info.getMethodsCondition().getMethods();
//            String methodName;
//            if(methodSet.size()!=0){
//                methodName=methodSet.iterator().next().name();
//            }else {
//                methodName="";
//            }
//            Method method = handlerMethod.getMethod();
//            if(method.isAnnotationPresent(Anonymous.class)){
//                Anonymous authorize = method.getAnnotation(Anonymous.class);
//                String roleName = authorize.role();
//                RoleUrl roleUrl = new RoleUrl(roleName,url,methodName);
//                roleResourceName.add(roleUrl);
//            }
//        }
//        List<Role> roleList = roleRepository.findAll();
//        List<Resource> resourceList = resourceRepository.findAll();
//        Map<String, Role> roleMap = ServiceUtil.convertToMap(roleList, Role::getEnName);
//        Map<String, Resource> resourceMap = resourceList.stream()
//                .collect(Collectors.toMap(resource -> resource.getMethod()+resource.getUrl(),resource -> resource));
//        roleResourceName.forEach(roleUrl->{
//            RoleResource roleResource = new RoleResource();
//            Role role = roleMap.get(roleUrl.getRole());
//            Resource resource = resourceMap.get(roleUrl.getMethod()+roleUrl.getUrl());
//            roleResource.setRoleId(role.getId());
//            roleResource.setResourceId(resource.getId());
//            roleResourceRepository.save(roleResource);
//        });
    }



//    @Override
//    public List<RoleResourceDTO> findByRoleId(Integer id) {
//        List<RoleResource> roleResources = roleResourceService.findByRoleId(id);
//        Set<Integer> resourceIds = ServiceUtil.fetchProperty(roleResources, RoleResource::getResourceId);
//        List<Resource> resources = findByIds(resourceIds);
//        return resources;
//    }
}
