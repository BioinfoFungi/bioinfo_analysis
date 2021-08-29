package com.wangyang.bioinfo.repository;

import com.wangyang.bioinfo.pojo.authorize.Role;
import com.wangyang.bioinfo.pojo.authorize.RoleResource;
import com.wangyang.bioinfo.pojo.authorize.UserRole;
import com.wangyang.bioinfo.repository.base.BaseRepository;
import com.wangyang.bioinfo.util.CacheStore;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.lang.Nullable;

import javax.persistence.QueryHint;
import java.util.List;
import java.util.Optional;

public interface UserRoleRepository extends BaseRepository<UserRole,Integer>{

    @Override
    @QueryHints({ @QueryHint(name = "org.hibernate.cacheable", value ="true") })
    List<UserRole> findAll();

    @Override
    @QueryHints({ @QueryHint(name = "org.hibernate.cacheable", value ="true") })
    Optional<UserRole> findById(Integer integer);
//    default  List<UserRole> listAll(){
//        List<UserRole> userRoles = CacheStore.getList("UserRole", UserRole.class);
//        if(userRoles==null){
//            userRoles = this.findAll();
//            CacheStore.save("UserRole",userRoles);
//        }
//        return userRoles;
//    }
}
