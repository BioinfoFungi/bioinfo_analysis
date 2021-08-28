package com.wangyang.bioinfo.service.impl;


import com.wangyang.bioinfo.pojo.authorize.UserRole;
import com.wangyang.bioinfo.repository.UserRoleRepository;
import com.wangyang.bioinfo.repository.base.BaseRepository;
import com.wangyang.bioinfo.service.IUserRoleService;
import com.wangyang.bioinfo.service.base.AbstractCrudService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class UserRoleServiceImpl extends AbstractCrudService<UserRole,Integer>
        implements IUserRoleService {


    private final UserRoleRepository userRoleRepository;
    public UserRoleServiceImpl(UserRoleRepository userRoleRepository) {
        super(userRoleRepository);
        this.userRoleRepository=userRoleRepository;
    }

    @Override
    public List<UserRole> listAll() {
        return userRoleRepository.listAll();
    }

    @Override
    public UserRole findBy(Integer userId, Integer roleId){
        List<UserRole> roleList = listAll().stream()
                .filter(userRole -> userRole.getUserId().equals(userId) && userRole.getRoleId().equals(roleId))
                .collect(Collectors.toList());
        return roleList.size()==0?null:roleList.get(0);
    }

    @Override
    public List<UserRole> findByUserId(Integer userId) {
        List<UserRole> roleList = listAll().stream()
                .filter(userRole -> userRole.getUserId().equals(userId))
                .collect(Collectors.toList());
        return roleList;
    }


    @Override
    public List<UserRole> findByRoleId(Integer roleId) {
        List<UserRole> roleList = listAll().stream()
                .filter(userRole -> userRole.getRoleId().equals(roleId))
                .collect(Collectors.toList());
        return roleList;
    }

    @Override
    public UserRole save(@RequestBody UserRole userRoleInput) {
        UserRole userRoles = findBy(userRoleInput.getUserId(), userRoleInput.getRoleId());
        if(userRoles==null){
            userRoles = super.save(userRoleInput);
        }
        return userRoles;
    }
}
