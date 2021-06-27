package com.wangyang.bioinfo.repository;

import com.wangyang.bioinfo.pojo.LncRNA;
import com.wangyang.bioinfo.pojo.MRNA;
import com.wangyang.bioinfo.pojo.MiRNA;
import com.wangyang.bioinfo.repository.base.BaseRNARepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * @author wangyang
 * @date 2021/6/27
 */
public interface MiRNARepository  extends BaseRNARepository<MiRNA> {
}
