package com.wangyang.bioinfo.repository;

import com.wangyang.bioinfo.pojo.authorize.Role;
import com.wangyang.bioinfo.pojo.authorize.UserRole;
import com.wangyang.bioinfo.repository.base.BaseRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface UserRoleRepository extends BaseRepository<UserRole,Integer>,
        JpaSpecificationExecutor<UserRole> {
}
