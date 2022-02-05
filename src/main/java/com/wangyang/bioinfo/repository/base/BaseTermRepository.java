package com.wangyang.bioinfo.repository.base;

import com.wangyang.bioinfo.pojo.entity.base.BaseTerm;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.NoRepositoryBean;

/**
 * @author wangyang
 * @date 2021/6/27
 */
@NoRepositoryBean
public interface BaseTermRepository<TERM extends BaseTerm> extends BaseRepository<TERM, Integer> {
}
