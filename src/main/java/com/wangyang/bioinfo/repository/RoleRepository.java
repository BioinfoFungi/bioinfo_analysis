package com.wangyang.bioinfo.repository;

import com.wangyang.bioinfo.pojo.Task;
import com.wangyang.bioinfo.pojo.authorize.Role;
import com.wangyang.bioinfo.repository.base.BaseRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.lang.Nullable;

import javax.persistence.QueryHint;
import java.util.List;

/**
 * @author wangyang
 * @date 2021/5/5
 */


public interface RoleRepository extends BaseRepository<Role,Integer>,
        JpaSpecificationExecutor<Role> {

    @QueryHints({ @QueryHint(name = "org.hibernate.cacheable", value ="true") })
    List<Role> findAll();
    @QueryHints({ @QueryHint(name = "org.hibernate.cacheable", value ="true") })
    List<Role> findAllById(Iterable<Integer> var1);
    @QueryHints({ @QueryHint(name = "org.hibernate.cacheable", value ="true") })
    List<Role> findAll(@Nullable Specification<Role> var1);
    @QueryHints({ @QueryHint(name = "org.hibernate.cacheable", value ="true") })
    Page<Role> findAll(@Nullable Specification<Role> var1, Pageable var2);
    @QueryHints({ @QueryHint(name = "org.hibernate.cacheable", value ="true") })
    Page<Role> findAll(Pageable var1);



}
