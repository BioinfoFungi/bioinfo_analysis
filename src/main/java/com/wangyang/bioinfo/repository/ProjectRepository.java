package com.wangyang.bioinfo.repository;

import com.wangyang.bioinfo.pojo.entity.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * @author wangyang
 * @date 2021/6/12
 */
public interface ProjectRepository extends JpaRepository<Project,Integer>
        , JpaSpecificationExecutor<Project> {
}
