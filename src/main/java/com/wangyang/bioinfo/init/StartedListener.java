package com.wangyang.bioinfo.init;

import com.google.common.collect.MapDifference;
import com.google.common.collect.Maps;
import com.wangyang.bioinfo.pojo.Option;
import com.wangyang.bioinfo.pojo.Task;
import com.wangyang.bioinfo.pojo.annotation.Authorize;
import com.wangyang.bioinfo.pojo.authorize.*;
import com.wangyang.bioinfo.pojo.dto.RoleUrl;
import com.wangyang.bioinfo.service.*;
import com.wangyang.bioinfo.util.ServiceUtil;
import com.wangyang.bioinfo.util.StringCacheStore;
import lombok.extern.slf4j.Slf4j;
import org.springframework.aop.support.AopUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.lang.reflect.Method;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author wangyang
 * @date 2021/6/14
 */
@Slf4j
@Configuration
public class StartedListener implements ApplicationListener<ApplicationStartedEvent> {
    @Value("${bioinfo.workDir}")
    private String workDir;
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
        List<Option> options = optionService.listAll();
        options.forEach(option -> {
            StringCacheStore.setValue(option.getKey_(),option.getValue_());
        });
        StringCacheStore.setValue("workDir",workDir);

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
        addQueue();
        initResource();
    }


    //添加数据库中的任务到队列
    public void addQueue(){
        List<Task> tasks = taskService.addTas2Queue();
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
            resource.setMethod(info.getMethodsCondition().getMethods().toString());
            resource.setUrl(url);
            Method method = handlerMethod.getMethod();
            if(method.isAnnotationPresent(Authorize.class)){
                Authorize authorize = method.getAnnotation(Authorize.class);
                String roleName = authorize.role();
                RoleUrl roleUrl = new RoleUrl(roleName,url);
                roleResourceName.add(roleUrl);
            }
            resources.add(resource);
        }
        List<Resource> existResource = resourceService.listAll();
        Map<String, Resource> systemResource = ServiceUtil.convertToMap(resources, Resource::getUrl);
        Map<String, Resource> convertToMap2 = ServiceUtil.convertToMap(existResource, Resource::getUrl);

        MapDifference<String, Resource> difference = Maps.difference(systemResource, convertToMap2);
        Map<String, Resource> onlyOnLeft = difference.entriesOnlyOnLeft();
        List<Resource> addResource = onlyOnLeft.values().stream().collect(Collectors.toList());
        resourceService.saveAll(addResource);
        Map<String, Resource> onlyOnRight = difference.entriesOnlyOnRight();
        List<Resource> removeResource = onlyOnRight.values().stream().collect(Collectors.toList());
        resourceService.deleteAll(removeResource);

        List<Role> roles = roleService.listAll();
        Map<String, Role> roleMap = ServiceUtil.convertToMap(roles, Role::getEnName);
        List<Resource> resourceALL = resourceService.listAll();
        Map<String, Resource> resourceMap = ServiceUtil.convertToMap(resourceALL, Resource::getUrl);

        roleResourceName.forEach(roleUrl->{
            RoleResource roleResource = new RoleResource();
            Role role = roleMap.get(roleUrl.getRole());
            Resource resource = resourceMap.get(roleUrl.getUrl());
            roleResource.setRoleId(role.getId());
            roleResource.setResourceId(resource.getId());
            roleResourceService.save(roleResource);
        });

    }
}
