package com.wangyang.bioinfo.web;

import com.wangyang.bioinfo.pojo.authorize.*;
import com.wangyang.bioinfo.pojo.dto.RoleDto;
import com.wangyang.bioinfo.service.IRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

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
    public List<RoleVO> findByRoleId(@PathVariable("id") Integer id){
        return  roleService.findByRoleId(id);
    }

    @GetMapping("/findByWithoutRoleId/{id}")
    public List<Role> findByWithoutRoleId(@PathVariable("id") Integer id){
        return roleService.findByWithoutRoleId(id);
    }

    @GetMapping("/findByUserId/{id}")
    public List<RoleVO> findByUserId(@PathVariable("id") Integer id){
        return  roleService.findByUserId(id);
    }

    @GetMapping("/findByWithoutUserId/{id}")
    public List<Role> findByWithoutUserId(@PathVariable("id") Integer id){
        return roleService.findByWithoutUserId(id);
    }

    @PostMapping
    public Role addRole(@RequestBody @Validated RoleParam roleParam){
        return roleService.addRole(roleParam);
    }

    @PostMapping("/update/{id}")
    public Role updateRole(@PathVariable("id") Integer id,@RequestBody @Validated RoleParam roleParam){
        return roleService.updateRole(id,roleParam);
    }

    @GetMapping("/del/{id}")
    public Role delRole(@PathVariable("id") Integer id){
        return roleService.delRole(id);
    }
    @GetMapping("/findById/{id}")
    public Role findById(@PathVariable("id") Integer id){
        return roleService.findById(id);
    }
}
