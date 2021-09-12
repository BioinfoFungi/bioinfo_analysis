package com.wangyang.bioinfo.repository.base;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.NoRepositoryBean;

import java.io.Serializable;
import java.util.List;

/**
 * @author wangyang
 * @date 2021/6/27
 */
@NoRepositoryBean
public interface BaseRepository <T,  ID extends Serializable> extends JpaRepository<T,  ID> {
    List<T> listAllCached();
    T saveCached(T entity);

}
