package com.wangyang.bioinfo.service.impl;


import com.wangyang.bioinfo.pojo.authorize.User;
import com.wangyang.bioinfo.pojo.authorize.UserRole;
import com.wangyang.bioinfo.repository.UserRepository;
import com.wangyang.bioinfo.repository.UserRoleRepository;
import com.wangyang.bioinfo.repository.base.BaseRepository;
import com.wangyang.bioinfo.service.IUserRoleService;
import com.wangyang.bioinfo.service.base.AbstractCrudService;
import com.wangyang.bioinfo.util.BioinfoException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserRoleServiceImpl extends AbstractCrudService<UserRole,Integer>
        implements IUserRoleService {


    private final UserRoleRepository userRoleRepository;
    public final UserRepository userRepository;
    public UserRoleServiceImpl(UserRoleRepository userRoleRepository,
                               UserRepository userRepository) {
        super(userRoleRepository);
        this.userRoleRepository=userRoleRepository;
        this.userRepository=userRepository;
    }

    @Override
    public List<UserRole> listAll() {
        return userRoleRepository.findAll();
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

    @Override
    public UserRole delBy(Integer id) {
        UserRole userRole = findById(id);
        Optional<User> userOptional = userRepository.findById(userRole.getUserId());
        if(userOptional.isPresent()&& userOptional.get().getUsername().equals("admin")){
            throw new BioinfoException("admin的权限不能被删除！");
        }
        return super.delBy(id);
    }
}
