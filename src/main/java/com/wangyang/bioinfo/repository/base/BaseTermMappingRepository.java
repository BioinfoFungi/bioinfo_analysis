package com.wangyang.bioinfo.repository.base;

import com.wangyang.bioinfo.pojo.entity.base.TermMapping;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.NoRepositoryBean;

/**
 * @author wangyang
 * @date 2021/7/25
 */
@NoRepositoryBean
public interface BaseTermMappingRepository<CATEGORY extends TermMapping> extends BaseRepository<CATEGORY, Integer>
    {

}
