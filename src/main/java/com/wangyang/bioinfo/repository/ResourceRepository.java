package com.wangyang.bioinfo.repository;

import com.wangyang.bioinfo.pojo.authorize.Resource;
import com.wangyang.bioinfo.pojo.authorize.Role;
import com.wangyang.bioinfo.repository.base.BaseRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * @author wangyang
 * @date 2021/5/5
 */
public interface ResourceRepository extends BaseRepository<Resource,Integer> ,
        JpaSpecificationExecutor<Role> {
}
