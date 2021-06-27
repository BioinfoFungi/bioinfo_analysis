package com.wangyang.bioinfo.repository;

import com.wangyang.bioinfo.pojo.DataOrigin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * @author wangyang
 * @date 2021/6/26
 */
public interface DataOriginRepository extends JpaRepository<DataOrigin,Integer>
        , JpaSpecificationExecutor<DataOrigin> {
}
