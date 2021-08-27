package com.wangyang.bioinfo.repository;

import com.wangyang.bioinfo.pojo.authorize.Role;
import com.wangyang.bioinfo.pojo.authorize.RoleResource;
import com.wangyang.bioinfo.pojo.authorize.UserRole;
import com.wangyang.bioinfo.repository.base.BaseRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.lang.Nullable;

import javax.persistence.QueryHint;
import java.util.List;

public interface UserRoleRepository extends BaseRepository<UserRole,Integer>,
        JpaSpecificationExecutor<UserRole> {
    @QueryHints({ @QueryHint(name = "org.hibernate.cacheable", value ="true") })
    List<UserRole> findAll();
    @QueryHints({ @QueryHint(name = "org.hibernate.cacheable", value ="true") })
    List<UserRole> findAllById(Iterable<Integer> var1);
    @QueryHints({ @QueryHint(name = "org.hibernate.cacheable", value ="true") })
    List<UserRole> findAll(@Nullable Specification<UserRole> var1);
    @QueryHints({ @QueryHint(name = "org.hibernate.cacheable", value ="true") })
    Page<UserRole> findAll(@Nullable Specification<UserRole> var1, Pageable var2);
    @QueryHints({ @QueryHint(name = "org.hibernate.cacheable", value ="true") })
    Page<UserRole> findAll(Pageable var1);
}
