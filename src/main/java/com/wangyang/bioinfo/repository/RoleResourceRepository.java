package com.wangyang.bioinfo.repository;

import com.wangyang.bioinfo.pojo.authorize.RoleResource;
import com.wangyang.bioinfo.pojo.authorize.UserRole;
import com.wangyang.bioinfo.repository.base.BaseRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface RoleResourceRepository extends BaseRepository<RoleResource,Integer>,
        JpaSpecificationExecutor<RoleResource> {
}
