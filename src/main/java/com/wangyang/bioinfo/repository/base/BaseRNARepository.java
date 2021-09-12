package com.wangyang.bioinfo.repository.base;

import com.wangyang.bioinfo.pojo.entity.base.BaseRNA;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.NoRepositoryBean;

/**
 * @author wangyang
 * @date 2021/6/27
 */
@NoRepositoryBean
public interface BaseRNARepository<T extends BaseRNA>  extends BaseRepository<T, Integer>
        ,JpaSpecificationExecutor<T> {
}
