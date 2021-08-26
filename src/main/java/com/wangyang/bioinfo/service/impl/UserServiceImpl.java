package com.wangyang.bioinfo.service.impl;

import com.wangyang.bioinfo.pojo.authorize.*;
import com.wangyang.bioinfo.pojo.dto.UserDto;
import com.wangyang.bioinfo.repository.UserRepository;
import com.wangyang.bioinfo.service.IRoleResourceService;
import com.wangyang.bioinfo.service.IRoleService;
import com.wangyang.bioinfo.service.IUserRoleService;
import com.wangyang.bioinfo.service.IUserService;
import com.wangyang.bioinfo.service.base.BaseAuthorizeServiceImpl;
import com.wangyang.bioinfo.util.BioinfoException;
import com.wangyang.bioinfo.util.ServiceUtil;
import com.wangyang.bioinfo.util.UserException;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author wangyang
 * @date 2021/5/5
 */
@Service
public class UserServiceImpl extends BaseAuthorizeServiceImpl<User>
        implements IUserService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    IUserRoleService userRoleService;
    @Autowired
    IRoleService roleService;

    @Override
    public User addUser(User user) {
        return userRepository.save(user);
    }

    @Override
    public User findUserById(int id) {
        Optional<User> userOptional = userRepository.findById(id);
        if(!userOptional.isPresent()){
            throw new BioinfoException("需要操作的用户不存在!!");
        }
        User user = userOptional.get();
        user.setPassword("");
        return  user;
    }

    @Override
    public List<User> findAllById(Collection<Integer> ids) {
        return userRepository.findAllById(ids);
    }


    @Override
    public List<UserDto> listAllUserDto() {
        List<UserDto> userDtos = userRepository.findAll().stream().map(user -> {
                    UserDto userDto = new UserDto();
                    BeanUtils.copyProperties(user,userDto);
                    return userDto;
                }
        ).collect(Collectors.toList());
        return userDtos;
    }

    @Override
    public User delUser(int id) {
        User user = findUserById(id);
        userRepository.delete(user);
        return user;
    }

    @Override
    public User updateUser(User user) {
        return null;
    }



    @Override
    public Page<User> pageUser(Pageable pageable) {
        return userRepository.findAll(pageable).map(user -> {
          User user1 = new User();
            user1.setId(user.getId());
//          user1.setRoles(user.getRoles());
            user1.setUsername(user.getUsername());
//          BeanUtils.copyProperties(user,user1);
          return user1;
        });
    }

    @Override
    public UserDetailDTO login(String username,String password) {
        UserDetailDTO userDetailDTO = new UserDetailDTO();
        User user = findUserByUsername(username);
        if(user==null){
            throw new UserException("用户名不存在！");
        }
        if(!user.getPassword().equals(password)){
            throw new UserException("用户名或密码错误！");
        }

        List<Role> roles = roleService.listAll();
        List<UserRole> userRoles = userRoleService.listAll();
        userRoles = userRoles.stream()
                .filter(userRole -> userRole.getUserId().equals(user.getId()))
                .collect(Collectors.toList());
        Set<Integer> roleIds = ServiceUtil.fetchProperty(userRoles, UserRole::getRoleId);
        Set<Role> usrRoles = roles.stream()
                .filter(role -> roleIds.contains(role.getId()))
                .collect(Collectors.toSet());
        userDetailDTO.setRoles(usrRoles);
        BeanUtils.copyProperties(user,userDetailDTO);
        return userDetailDTO;
    }

    @Override
    public User findUserByUsername(String username) {
        List<User> users = userRepository.findAll(new Specification<User>() {
            @Override
            public Predicate toPredicate(Root<User> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                return criteriaQuery.where(criteriaBuilder.equal(root.get("username"), username)).getRestriction();
            }
        });
        return CollectionUtils.isEmpty(users)?null:users.get(0);
    }
}
