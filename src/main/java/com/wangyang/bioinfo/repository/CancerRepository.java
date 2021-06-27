package com.wangyang.bioinfo.repository;

import com.wangyang.bioinfo.pojo.Cancer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * @author wangyang
 * @date 2021/6/26
 */
public interface CancerRepository extends JpaRepository<Cancer,Integer>
        , JpaSpecificationExecutor<Cancer> {
}
