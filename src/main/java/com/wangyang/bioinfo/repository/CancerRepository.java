package com.wangyang.bioinfo.repository;

import com.wangyang.bioinfo.pojo.Cancer;
import com.wangyang.bioinfo.pojo.Study;
import com.wangyang.bioinfo.repository.base.BaseTermRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * @author wangyang
 * @date 2021/6/26
 */
public interface CancerRepository  extends BaseTermRepository<Cancer> {
}
