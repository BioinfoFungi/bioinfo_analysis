package com.wangyang.bioinfo.repository.base;

import com.wangyang.bioinfo.pojo.base.BaseTerm;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.NoRepositoryBean;

/**
 * @author wangyang
 * @date 2021/6/27
 */
@NoRepositoryBean
public interface BaseTermRepository<TERM extends BaseTerm> extends BaseRepository<TERM, Integer>
        , JpaSpecificationExecutor<TERM> {
}
