package com.wangyang.bioinfo.repository.geo;

import com.wangyang.bioinfo.pojo.entity.geo.GPLSupport;
import com.wangyang.bioinfo.pojo.entity.geo.GSMSupport;
import com.wangyang.bioinfo.repository.base.BaseRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface GSMSupportRepository extends BaseRepository<GSMSupport,Integer>
        , JpaSpecificationExecutor<GSMSupport> {
}
