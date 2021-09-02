package com.wangyang.bioinfo.init;

import com.google.common.collect.MapDifference;
import com.google.common.collect.Maps;
import com.wangyang.bioinfo.pojo.Option;
import com.wangyang.bioinfo.pojo.Task;
import com.wangyang.bioinfo.pojo.annotation.Anonymous;
import com.wangyang.bioinfo.pojo.authorize.*;
import com.wangyang.bioinfo.pojo.dto.RoleUrl;
import com.wangyang.bioinfo.pojo.enums.TaskStatus;
import com.wangyang.bioinfo.service.*;
import com.wangyang.bioinfo.util.ServiceUtil;
import com.wangyang.bioinfo.util.CacheStore;
import com.wangyang.bioinfo.util.TokenProvider;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;


import javax.cache.CacheManager;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author wangyang
 * @date 2021/6/14
 */
@Slf4j
@Configuration
@Order(Ordered.HIGHEST_PRECEDENCE)
public class StartedListener implements ApplicationListener<ApplicationStartedEvent> {
    @Value("${bioinfo.workDir}")
    private String workDir;

    @Value("${bioinfo.authorizeInit}")
    private Boolean authorizeInit;

    @Autowired
    private CacheManager cacheManager;
    @Autowired
    IOptionService optionService;
    @Autowired
    ICodeService codeService;
    @Autowired
    ITaskService taskService;
    @Autowired
    ThreadPoolTaskExecutor executor;
    @Autowired
    ICancerStudyService cancerStudyService;

    @Autowired
    IUserService userService;

    @Autowired
    IResourceService resourceService;
    @Autowired
    IRoleService roleService;
    @Autowired
    IUserRoleService userRoleService;

    @Autowired
    IRoleResourceService roleResourceService;

    @Autowired
    private ApplicationContext applicationContext;


    @Override
    public void onApplicationEvent(ApplicationStartedEvent applicationStartedEvent) {
        System.out.println("################init########################");
        System.out.println(StringUtils.join((Collection) cacheManager.getCacheNames(), ","));
        List<Option> options = optionService.listAll();
        options.forEach(option -> {
            CacheStore.setValue(option.getKey_(),option.getValue_());
        });
        CacheStore.setValue("workDir",workDir);
        addQueue();
        initResource();
    }


    //添加数据库中的任务到队列
    public void addQueue(){
        List<Task> tasks = taskService.addTas2Queue();
        tasks.forEach(task->{
            if(!task.getTaskStatus().equals(TaskStatus.FINISH)){
                task.setTaskStatus(TaskStatus.INTERRUPT);
                taskService.save(task);
            }
        });
        int size = executor.getThreadPoolExecutor().getQueue().size();
        int activeCount = executor.getActiveCount();
        int corePoolSize = executor.getCorePoolSize();
        int maxPoolSize = executor.getMaxPoolSize();
        int keepAliveSeconds = executor.getKeepAliveSeconds();
        log.info(">>>>> Thread Pool size: {}",size);
        log.info(">>>>> active count: {}",activeCount);
        log.info(">>>>> core pool size: {}",corePoolSize);
        log.info(">>>>> max  pool size: {}",maxPoolSize);
        log.info(">>>>> keep alive seconds: {}",keepAliveSeconds);
    }

    public void initResource(){
        Role role = roleService.findByEnName("ADMIN");
        if(role==null){
            role = new Role();
            role.setName("ADMIN");
            role.setEnName("ADMIN");
            role = roleService.save(role);
        }
        User user = userService.findUserByUsername("admin");

        if(user==null){
            user = new User();
            user.setUsername("admin");
            user.setGender(0);
            user.setPassword("123456");
            userService.addUser(user);
            UserRole userRole = new UserRole(user.getId(),role.getId());
            userRoleService.save(userRole);
        }
//        Map<String, Object> controllers = applicationContext.getBeansWithAnnotation(RequestMapping.class);
        RequestMappingHandlerMapping mapping = applicationContext.getBean("requestMappingHandlerMapping",RequestMappingHandlerMapping.class);
        Map<RequestMappingInfo, HandlerMethod> methodMap = mapping.getHandlerMethods();
        List<Resource> resources = new ArrayList<>();
        List<RoleUrl> roleResourceName = new ArrayList<>();
        for (RequestMappingInfo info : methodMap.keySet()){
            Resource resource = new Resource();
            HandlerMethod handlerMethod = methodMap.get(info);
            Set<String> urlSet = info.getPatternsCondition().getPatterns();
            String url = urlSet.iterator().next();
            if (url.equals("/error"))continue;
            Set<RequestMethod> methodSet = info.getMethodsCondition().getMethods();
            String methodName;
            if(methodSet.size()!=0){
                methodName=methodSet.iterator().next().name();
            }else {
                methodName="";
            }


            resource.setMethod(methodName);
            resource.setUrl(url);
            Method method = handlerMethod.getMethod();
            if(!method.isAnnotationPresent(Anonymous.class)){
//                Anonymous authorize = method.getAnnotation(Anonymous.class);
//                String roleName = authorize.role();
                RoleUrl roleUrl = new RoleUrl(role.getId(),url,methodName);
                roleResourceName.add(roleUrl);
            }
            resources.add(resource);
        }
        List<Resource> dataBaseResource = resourceService.listAll();
        Map<String, Resource> systemResourceMap = resources.stream()
                .collect(Collectors.toMap(resource -> resource.getMethod()+resource.getUrl(),resource -> resource));
        Map<String, Resource> dataBaseResourceMap = dataBaseResource.stream()
                .collect(Collectors.toMap(resource -> resource.getMethod()+resource.getUrl(),resource -> resource));

        MapDifference<String, Resource> difference = Maps.difference(systemResourceMap, dataBaseResourceMap);
        Map<String, Resource> onlyOnLeft = difference.entriesOnlyOnLeft();
        List<Resource> addResource = onlyOnLeft.values().stream().collect(Collectors.toList());
        resourceService.saveAll(addResource);
        Map<String, Resource> onlyOnRight = difference.entriesOnlyOnRight();
        List<Resource> removeResource = onlyOnRight.values().stream().collect(Collectors.toList());
        resourceService.deleteAll(removeResource);

        if(authorizeInit){
            List<Role> roles = roleService.listAll();
            Map<Integer, Role> roleMap = ServiceUtil.convertToMap(roles, Role::getId);
            List<Resource> resourceResult = resourceService.listAll();
            Map<String, Resource> resourceMap = resourceResult.stream()
                    .collect(Collectors.toMap(resource -> resource.getMethod()+resource.getUrl(),resource -> resource));

            List<RoleResource> roleResources = roleResourceName.stream().map(
                    roleUrl -> {
                        RoleResource roleResource = new RoleResource();
                        Integer roleId = roleUrl.getRoleId();
                        Resource resource = resourceMap.get(roleUrl.getMethod() + roleUrl.getUrl());
                        roleResource.setResourceId(resource.getId());
                        roleResource.setRoleId(roleId);
                        return roleResource;
                    }).collect(Collectors.toList());
            roleResourceService.deleteAll();
            roleResourceService.saveAll(roleResources);
//            List<RoleResource> databaseRoleResources = roleResourceService.listAll();
//            roleResources.containsAll(databaseRoleResources);
//
//            roleResourceName.forEach(roleUrl->{
//                RoleResource roleResource = new RoleResource();
//                Resource resource = resourceMap.get(roleUrl.getMethod()+roleUrl.getUrl());
//                roleResource.setRoleId(roleUrl.getRoleId());
//                roleResource.setResourceId(resource.getId());
//                roleResourceService.save(roleResource);
//            });
        }
    }
}
