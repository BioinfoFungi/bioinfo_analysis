package com.wangyang.bioinfo.repository;

import com.wangyang.bioinfo.pojo.authorize.Resource;
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
public interface ResourceRepository extends BaseRepository<Resource,Integer> ,
        JpaSpecificationExecutor<Resource> {

    @QueryHints({ @QueryHint(name = "org.hibernate.cacheable", value ="true") })
    List<Resource> findAll();
    @QueryHints({ @QueryHint(name = "org.hibernate.cacheable", value ="true") })
    List<Resource> findAllById(Iterable<Integer> var1);
    @QueryHints({ @QueryHint(name = "org.hibernate.cacheable", value ="true") })
    List<Resource> findAll(@Nullable Specification<Resource> var1);
    @QueryHints({ @QueryHint(name = "org.hibernate.cacheable", value ="true") })
    Page<Resource> findAll(@Nullable Specification<Resource> var1, Pageable var2);
    @QueryHints({ @QueryHint(name = "org.hibernate.cacheable", value ="true") })
    Page<Resource> findAll(Pageable var1);
}
