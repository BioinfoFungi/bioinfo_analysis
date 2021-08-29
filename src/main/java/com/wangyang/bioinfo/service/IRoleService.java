package com.wangyang.bioinfo.service;

import com.wangyang.bioinfo.pojo.Task;
import com.wangyang.bioinfo.pojo.authorize.Role;
import com.wangyang.bioinfo.pojo.authorize.RoleParam;
import com.wangyang.bioinfo.pojo.authorize.RoleVO;
import com.wangyang.bioinfo.pojo.dto.RoleDto;
import com.wangyang.bioinfo.service.base.ICrudService;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
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

    Role findByEnName(String name);

    List<RoleVO> findByUserId(Integer id);

    List<Role> findByWithoutUserId(Integer id);

    List<RoleVO> findByRoleId(Integer id);

    List<Role> findByWithoutRoleId(Integer id);

    Role addRole(RoleParam roleParam);

    Role updateRole(Integer id, RoleParam roleParam);

//    List<Role> listAll();

}
