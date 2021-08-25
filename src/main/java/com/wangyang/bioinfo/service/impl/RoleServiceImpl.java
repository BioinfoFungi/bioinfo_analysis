package com.wangyang.bioinfo.service.impl;

import com.wangyang.bioinfo.pojo.authorize.Role;
import com.wangyang.bioinfo.pojo.dto.RoleDto;
import com.wangyang.bioinfo.repository.RoleRepository;
import com.wangyang.bioinfo.service.IRoleService;
import com.wangyang.bioinfo.service.base.AbstractCrudService;
import com.wangyang.bioinfo.util.BioinfoException;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * @author wangyang
 * @date 2021/5/5
 */
@Service
public class RoleServiceImpl extends AbstractCrudService<Role,Integer>
            implements IRoleService {

    @Autowired
    RoleRepository roleRepository;


    @Override
    public Role addRole(Role role) {
        return roleRepository.save(role);
    }

    @Override
    public Role findRoleById(int id) {
        Optional<Role> roleOptional = roleRepository.findById(id);
        return roleOptional.isPresent()?roleOptional.get():null;
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
        List<Role> roles = roleRepository.findAll(new Specification<Role>() {
            @Override
            public Predicate toPredicate(Root<Role> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                return criteriaQuery.where(criteriaBuilder.equal(root.get("enName"),name)).getRestriction();
            }
        });
        return roles.size()==0?null:roles.get(0);
    }

    @Override
    @Cacheable(cacheNames = {"AUTHORIZE_ROLE"})
    public List<Role> listAll() {
        return super.listAll();
    }
}
