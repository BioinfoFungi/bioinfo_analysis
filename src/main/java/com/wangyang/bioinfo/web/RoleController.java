package com.wangyang.bioinfo.web;

import com.wangyang.bioinfo.pojo.authorize.Resource;
import com.wangyang.bioinfo.pojo.authorize.Role;
import com.wangyang.bioinfo.pojo.dto.RoleDto;
import com.wangyang.bioinfo.service.IRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static org.springframework.data.domain.Sort.Direction.DESC;

/**
 * @author wangyang
 * @date 2021/5/5
 */
@RestController
@RequestMapping("/api/role")
public class RoleController {

    @Autowired
    IRoleService roleService;
    @GetMapping
    public Page<Role> page(@PageableDefault(sort = {"id"},direction = DESC) Pageable pageable){
        return roleService.pageBy(pageable);
    }
    @GetMapping("/listAll")
    public List<Role> listRole(){
        return roleService.listAll();
    }

    @GetMapping("/findByRoleId/{id}")
    public List<Role> findByUserId(@PathVariable("id") Integer id){
        return  roleService.findByUserId(id);
    }
}
