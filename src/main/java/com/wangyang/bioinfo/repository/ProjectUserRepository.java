package com.wangyang.bioinfo.repository;


import com.wangyang.bioinfo.pojo.entity.ProjectUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * @author wangyang
 * @date 2021/6/14
 */
public interface ProjectUserRepository extends JpaRepository<ProjectUser,Integer>
        , JpaSpecificationExecutor<ProjectUser> {
}
