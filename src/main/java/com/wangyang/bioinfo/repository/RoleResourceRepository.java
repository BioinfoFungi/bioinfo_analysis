package com.wangyang.bioinfo.repository;

import com.wangyang.bioinfo.pojo.authorize.Resource;
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

public interface RoleResourceRepository extends BaseRepository<RoleResource,Integer>,
        JpaSpecificationExecutor<RoleResource> {
//    @QueryHints({ @QueryHint(name = "org.hibernate.cacheable", value ="true") })
//    List<RoleResource> findAll();
//    @QueryHints({ @QueryHint(name = "org.hibernate.cacheable", value ="true") })
//    List<RoleResource> findAllById(Iterable<Integer> var1);
//    @QueryHints({ @QueryHint(name = "org.hibernate.cacheable", value ="true") })
//    List<RoleResource> findAll(@Nullable Specification<RoleResource> var1);
//    @QueryHints({ @QueryHint(name = "org.hibernate.cacheable", value ="true") })
//    Page<RoleResource> findAll(@Nullable Specification<RoleResource> var1, Pageable var2);
//    @QueryHints({ @QueryHint(name = "org.hibernate.cacheable", value ="true") })
//    Page<RoleResource> findAll(Pageable var1);
}
