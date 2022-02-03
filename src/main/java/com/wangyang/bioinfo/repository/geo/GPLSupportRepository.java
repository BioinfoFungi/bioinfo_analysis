package com.wangyang.bioinfo.repository.geo;

import com.wangyang.bioinfo.pojo.entity.geo.GPLSupport;
import com.wangyang.bioinfo.repository.base.BaseRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface GPLSupportRepository extends BaseRepository<GPLSupport,Integer>
        , JpaSpecificationExecutor<GPLSupport> {
}
