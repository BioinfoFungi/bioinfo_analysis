package com.wangyang.bioinfo.repository;

import com.wangyang.bioinfo.pojo.Task;
import com.wangyang.bioinfo.pojo.authorize.Resource;
import com.wangyang.bioinfo.pojo.authorize.Role;
import com.wangyang.bioinfo.pojo.authorize.UserRole;
import com.wangyang.bioinfo.repository.base.BaseRepository;
import com.wangyang.bioinfo.util.CacheStore;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.lang.Nullable;

import javax.persistence.QueryHint;
import java.util.List;
import java.util.Optional;

/**
 * @author wangyang
 * @date 2021/5/5
 */


public interface RoleRepository extends BaseRepository<Role,Integer>{
    @Override
    @QueryHints({ @QueryHint(name = "org.hibernate.cacheable", value ="true") })
    List<Role> findAll();

    @Override
    @QueryHints({ @QueryHint(name = "org.hibernate.cacheable", value ="true") })
    Optional<Role> findById(Integer integer);
}
