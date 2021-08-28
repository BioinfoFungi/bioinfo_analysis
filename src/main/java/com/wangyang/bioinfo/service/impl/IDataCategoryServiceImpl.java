package com.wangyang.bioinfo.service.impl;

import com.wangyang.bioinfo.pojo.trem.DataCategory;
import com.wangyang.bioinfo.repository.DataCategoryRepository;
import com.wangyang.bioinfo.repository.base.BaseTermRepository;
import com.wangyang.bioinfo.service.IDataCategoryService;
import com.wangyang.bioinfo.service.base.BaseTermServiceImpl;
import org.springframework.stereotype.Service;

/**
 * @author wangyang
 * @date 2021/7/25
 */
@Service
public class IDataCategoryServiceImpl extends BaseTermServiceImpl<DataCategory>
        implements IDataCategoryService {
    private final DataCategoryRepository dataCategoryRepository;
    public IDataCategoryServiceImpl(DataCategoryRepository dataCategoryRepository) {
        super(dataCategoryRepository);
        this.dataCategoryRepository = dataCategoryRepository;
    }
}
