package com.wangyang.bioinfo.repository;

import com.wangyang.bioinfo.pojo.authorize.Resource;
import com.wangyang.bioinfo.pojo.authorize.Role;
import com.wangyang.bioinfo.pojo.authorize.RoleResource;
import com.wangyang.bioinfo.pojo.authorize.UserRole;
import com.wangyang.bioinfo.repository.base.BaseRepository;
import com.wangyang.bioinfo.util.CacheStore;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.lang.Nullable;

import javax.persistence.QueryHint;
import java.util.List;
import java.util.Optional;

public interface RoleResourceRepository extends BaseRepository<RoleResource,Integer> {

    @Override
    @QueryHints({ @QueryHint(name = "org.hibernate.cacheable", value ="true") })
    List<RoleResource> findAll();

    @Override
    @QueryHints({ @QueryHint(name = "org.hibernate.cacheable", value ="true") })
    Optional<RoleResource> findById(Integer integer);
//    default  List<RoleResource> listAll(){
//        List<RoleResource> roleResource = CacheStore.getList("RoleResource", RoleResource.class);
//        if(roleResource==null){
//            roleResource = this.findAll();
//            CacheStore.save("RoleResource",roleResource);
//        }
//        return roleResource;
//    }
}
