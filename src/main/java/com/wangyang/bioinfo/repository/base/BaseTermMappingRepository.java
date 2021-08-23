package com.wangyang.bioinfo.repository.base;

import com.wangyang.bioinfo.pojo.file.TermMapping;
import org.springframework.data.repository.NoRepositoryBean;

/**
 * @author wangyang
 * @date 2021/7/25
 */
@NoRepositoryBean
public interface BaseTermMappingRepository<CATEGORY extends TermMapping> extends BaseFileRepository<CATEGORY> {

}
