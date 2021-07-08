package com.wangyang.bioinfo.repository;

import com.wangyang.bioinfo.pojo.CancerStudy;
import com.wangyang.bioinfo.repository.base.BaseFileRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * @author wangyang
 * @date 2021/6/26
 */
public interface CancerStudyRepository  extends BaseFileRepository<CancerStudy> {
}
