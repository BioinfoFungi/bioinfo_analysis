package com.wangyang.bioinfo.repository;

import com.wangyang.bioinfo.pojo.Study;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * @author wangyang
 * @date 2021/6/26
 */
public interface StudyRepository  extends JpaRepository<Study,Integer>
        , JpaSpecificationExecutor<Study> {
}
