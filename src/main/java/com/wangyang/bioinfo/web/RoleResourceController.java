package com.wangyang.bioinfo.web;

import com.wangyang.bioinfo.pojo.authorize.RoleResource;
import com.wangyang.bioinfo.service.IRoleResourceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/role_resource")
public class RoleResourceController {
    @Autowired
    IRoleResourceService roleResourceService;

    @PostMapping
    public RoleResource save(@RequestBody RoleResource resource){
        return roleResourceService.save(resource);
    }
}
