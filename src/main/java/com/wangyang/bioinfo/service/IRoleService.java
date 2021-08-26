package com.wangyang.bioinfo.service;

import com.wangyang.bioinfo.pojo.Task;
import com.wangyang.bioinfo.pojo.authorize.Role;
import com.wangyang.bioinfo.pojo.dto.RoleDto;
import com.wangyang.bioinfo.service.base.ICrudService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Set;

/**
 * @author wangyang
 * @date 2021/5/5
 */
public interface IRoleService  extends ICrudService<Role, Integer> {
    Role addRole(Role role);
    Role findRoleById(int id);
    Role delRole(int id);
    Page<RoleDto> pageRole(Pageable pageable);
    Role updateRole(Role role);

    Role findByEnName(String name);

    List<Role> findByUserId(Integer id);

//    List<Role> listAll();

}
