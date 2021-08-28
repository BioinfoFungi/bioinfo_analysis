package com.wangyang.bioinfo.service.impl;

import com.wangyang.bioinfo.pojo.authorize.Resource;
import com.wangyang.bioinfo.pojo.authorize.Role;
import com.wangyang.bioinfo.pojo.authorize.RoleResource;
import com.wangyang.bioinfo.pojo.authorize.UserRole;
import com.wangyang.bioinfo.pojo.dto.RoleDto;
import com.wangyang.bioinfo.repository.RoleRepository;
import com.wangyang.bioinfo.repository.base.BaseRepository;
import com.wangyang.bioinfo.service.IRoleService;
import com.wangyang.bioinfo.service.IUserRoleService;
import com.wangyang.bioinfo.service.base.AbstractCrudService;
import com.wangyang.bioinfo.util.BioinfoException;
import com.wangyang.bioinfo.util.ServiceUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.QueryHint;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.transaction.Transactional;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author wangyang
 * @date 2021/5/5
 */
@Service
//@Transactional
public class RoleServiceImpl extends AbstractCrudService<Role,Integer>
            implements IRoleService {


    private final RoleRepository roleRepository;
    private final IUserRoleService userRoleService;

    public RoleServiceImpl(RoleRepository roleRepository,
                           IUserRoleService userRoleService) {
        super(roleRepository);
        this.roleRepository=roleRepository;
        this.userRoleService=userRoleService;
    }


    @Override
    public List<Role> listAll() {
        return roleRepository.listAll();
    }

    @Override
    public Role addRole(Role role) {
        return super.save(role);
    }

    @Override
    public Role findRoleById(int id) {
        List<Role> roleList = listAll().stream()
                .filter(role -> role.getId() == id)
                .collect(Collectors.toList());
        return roleList.size()==0?null:roleList.get(0);
    }

    @Override
    public Role delRole(int id) {
        Role role = findRoleById(id);
        if(role==null){
            throw new BioinfoException("要删除的角色不存在");
        }
        roleRepository.delete(role);
        return role;
    }

    @Override
    public Page<RoleDto> pageRole(Pageable pageable) {
        return roleRepository.findAll(pageable).map(role -> {
            RoleDto roleDto = new RoleDto();
            BeanUtils.copyProperties(role,roleDto);
            return roleDto;
        });
    }

    @Override
    public Role updateRole(Role role) {
        return null;
    }

    @Override
    public Role findByEnName(String name){
        List<Role> roleList = listAll().stream()
                .filter(role -> role.getEnName().equals(name))
                .collect(Collectors.toList());
        return roleList.size()==0?null:roleList.get(0);
    }


    public List<Role> findByIds(Iterable<Integer> inputIds){
        Set<Integer> ids = (Set<Integer> )inputIds;
        List<Role> roleList = listAll().stream()
                .filter(role -> ids.contains(role.getId()))
                .collect(Collectors.toList());
        return roleList;
    }


    @Override
    public List<Role> findByUserId(Integer id) {
        List<UserRole> userRoles = userRoleService.findByUserId(id);
        Set<Integer> resourceIds = ServiceUtil.fetchProperty(userRoles, UserRole::getRoleId);
        List<Role> roles = findByIds(resourceIds);
        return roles;
    }
}
