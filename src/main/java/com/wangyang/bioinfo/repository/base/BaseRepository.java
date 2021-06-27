package com.wangyang.bioinfo.repository.base;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.NoRepositoryBean;

/**
 * @author wangyang
 * @date 2021/6/27
 */
@NoRepositoryBean
public interface BaseRepository <DOMAIN, ID> extends JpaRepository<DOMAIN, ID> {
}
