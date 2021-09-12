package com.wangyang.bioinfo.repository;

import com.wangyang.bioinfo.pojo.entity.CancerStudy;
import com.wangyang.bioinfo.repository.base.BaseTermMappingRepository;

/**
 * @author wangyang
 * @date 2021/6/26
 */
public interface CancerStudyRepository  extends BaseTermMappingRepository<CancerStudy> {
    CancerStudy findByParentIdAndCodeId(Integer id, Integer codeId);
}
