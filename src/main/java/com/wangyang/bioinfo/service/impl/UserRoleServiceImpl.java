package com.wangyang.bioinfo.service.impl;

import com.wangyang.bioinfo.pojo.authorize.RoleResource;
import com.wangyang.bioinfo.pojo.authorize.User;
import com.wangyang.bioinfo.pojo.authorize.UserRole;
import com.wangyang.bioinfo.repository.UserRepository;
import com.wangyang.bioinfo.repository.UserRoleRepository;
import com.wangyang.bioinfo.service.IUserRoleService;
import com.wangyang.bioinfo.service.base.AbstractCrudService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.List;

@Service
public class UserRoleServiceImpl extends AbstractCrudService<UserRole,Integer>
        implements IUserRoleService {

    @Autowired
    UserRoleRepository  userRoleRepository;

    @Override
    @Cacheable(cacheNames = {"AUTHORIZE_USR_ROLE"})
    public List<UserRole> listAll() {

        return super.listAll();
    }
    @Override
    public UserRole findBy(Integer userId, Integer roleId){
        List<UserRole> userRoles = userRoleRepository.findAll(new Specification<UserRole>() {
            @Override
            public Predicate toPredicate(Root<UserRole> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                return criteriaQuery.where(
                        criteriaBuilder.equal(root.get("userId"),userId),
                        criteriaBuilder.equal(root.get("roleId"),roleId)
                ).getRestriction();
            }
        });
        return userRoles.size()==0?null:userRoles.get(0);
    }

    @Override
    public List<UserRole> findByUserId(Integer userId) {
        List<UserRole> userRoles = userRoleRepository.findAll(new Specification<UserRole>() {
            @Override
            public Predicate toPredicate(Root<UserRole> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                return criteriaQuery.where(
                        criteriaBuilder.equal(root.get("userId"),userId)
                ).getRestriction();
            }
        });
        return userRoles;
    }

    public UserRole findBy(int userId, int roleId){
        List<UserRole> userRoles = userRoleRepository.findAll(new Specification<UserRole>() {
            @Override
            public Predicate toPredicate(Root<UserRole> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                return criteriaQuery.where(
                        criteriaBuilder.equal(root.get("userId"),userId),
                        criteriaBuilder.equal(root.get("roleId"),roleId)
                ).getRestriction();
            }
        });
        return userRoles.size()==0?null:userRoles.get(0);
    }

    @Override
    public UserRole save(@RequestBody UserRole userRoleInput) {
        UserRole userRoles = findBy(userRoleInput.getUserId(), userRoleInput.getRoleId());
        if(userRoles==null){
            userRoles = super.save(userRoles);
        }
        return userRoles;
    }
}
