package com.wangyang.bioinfo.repository.base;

import com.wangyang.bioinfo.pojo.base.BaseFile;
import com.wangyang.bioinfo.pojo.file.BaseDataCategory;
import org.springframework.data.repository.NoRepositoryBean;

/**
 * @author wangyang
 * @date 2021/7/25
 */
@NoRepositoryBean
public interface BaseDataCategoryRepository<CATEGORY extends BaseDataCategory> extends BaseFileRepository<CATEGORY> {

}
