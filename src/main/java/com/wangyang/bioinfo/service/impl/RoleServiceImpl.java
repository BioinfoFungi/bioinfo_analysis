package com.wangyang.bioinfo.service.impl;

import com.wangyang.bioinfo.pojo.authorize.*;
import com.wangyang.bioinfo.pojo.dto.RoleDto;
import com.wangyang.bioinfo.repository.RoleRepository;
import com.wangyang.bioinfo.repository.base.BaseRepository;
import com.wangyang.bioinfo.service.IRoleResourceService;
import com.wangyang.bioinfo.service.IRoleService;
import com.wangyang.bioinfo.service.IUserRoleService;
import com.wangyang.bioinfo.service.base.AbstractCrudService;
import com.wangyang.bioinfo.util.BioinfoException;
import com.wangyang.bioinfo.util.ServiceUtil;
import lombok.extern.slf4j.Slf4j;
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
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author wangyang
 * @date 2021/5/5
 */
@Service
//@Transactional
@Slf4j
public class RoleServiceImpl extends AbstractCrudService<Role,Integer>
            implements IRoleService {


    private final RoleRepository roleRepository;
    private final IUserRoleService userRoleService;
    private final IRoleResourceService roleResourceService;

    public RoleServiceImpl(RoleRepository roleRepository,
                           IUserRoleService userRoleService,
                           IRoleResourceService roleResourceService) {
        super(roleRepository);
        this.roleRepository=roleRepository;
        this.userRoleService=userRoleService;
        this.roleResourceService=roleResourceService;
    }


    @Override
    public List<Role> listAll() {
        return roleRepository.findAll();
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
        Role role = findById(id);
        if(role.getEnName().equals("ADMIN")){
            throw new BioinfoException("ADMIN角色不能删除！");
        }
        List<RoleResource> roleResources = roleResourceService.findByRoleId(role.getId());
        roleResourceService.deleteAll(roleResources);
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
    public Role addRole(RoleParam roleParam) {
        Role role = new Role();
        BeanUtils.copyProperties(roleParam,role);
        return roleRepository.save(role);
    }

    @Override
    public Role updateRole(Integer id, RoleParam roleParam) {
        Role role = findById(id);
        BeanUtils.copyProperties(roleParam,role);
        return roleRepository.save(role);
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
    public List<Role> findByWithoutIds(Iterable<Integer> inputIds){
        Set<Integer> ids = (Set<Integer> )inputIds;
        List<Role> roleList = listAll().stream()
                .filter(role -> !ids.contains(role.getId()))
                .collect(Collectors.toList());
        return roleList;
    }

    @Override
    public List<RoleVO> findByUserId(Integer id) {
        List<UserRole> userRoles = userRoleService.findByUserId(id);
        Set<Integer> roleIds = ServiceUtil.fetchProperty(userRoles, UserRole::getRoleId);
        List<Role> roles = findByIds(roleIds);
        Map<Integer, Role> roleMap = ServiceUtil.convertToMap(roles, Role::getId);
        List<RoleVO> resourceVOS = userRoles.stream().map(userRole -> {
            RoleVO roleVO = new RoleVO();
            Role role = roleMap.get(userRole.getRoleId());
            BeanUtils.copyProperties(role,roleVO);
            roleVO.setUserRoleId(userRole.getId());
            roleVO.setUserId( userRole.getUserId());

            return roleVO;
        }).collect(Collectors.toList());
        return resourceVOS;
    }

    @Override
    public List<Role> findByWithoutUserId(Integer id) {
        List<UserRole> userRoles = userRoleService.findByUserId(id);
        Set<Integer> roleIds = ServiceUtil.fetchProperty(userRoles, UserRole::getRoleId);
        List<Role> roles = findByWithoutIds(roleIds);
        return roles;
    }




    @Override
    public List<RoleVO> findByRoleId(Integer id) {
        List<RoleResource> roleResources = roleResourceService.findByResourceId(id);
        Set<Integer> roleIds = ServiceUtil.fetchProperty(roleResources, RoleResource::getRoleId);
        List<Role> roles = findByIds(roleIds);
        Map<Integer, Role> resourceMap = ServiceUtil.convertToMap(roles, Role::getId);

        List<RoleVO> resourceVOS = roleResources.stream().map(roleResource -> {
            RoleVO roleVO = new RoleVO();
            Role role = resourceMap.get(roleResource.getRoleId());
            BeanUtils.copyProperties(role,roleVO);
            roleVO.setResourceRoleId(roleResource.getId());
            roleVO.setResourceId( roleResource.getResourceId());

            return roleVO;
        }).collect(Collectors.toList());
        return resourceVOS;
    }

    @Override
    public List<Role> findByWithoutRoleId(Integer id) {
        List<RoleResource> roleResources = roleResourceService.findByRoleId(id);
        Set<Integer> roleIds = ServiceUtil.fetchProperty(roleResources, RoleResource::getResourceId);
        List<Role> roles = findByWithoutIds(roleIds);
        return roles;
    }

}
