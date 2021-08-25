package com.wangyang.bioinfo.web;

import com.wangyang.bioinfo.pojo.dto.RoleDto;
import com.wangyang.bioinfo.service.IRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    public Page<RoleDto> page(@PageableDefault(sort = {"id"},direction = DESC) Pageable pageable){
        return roleService.pageRole(pageable);
    }
}
