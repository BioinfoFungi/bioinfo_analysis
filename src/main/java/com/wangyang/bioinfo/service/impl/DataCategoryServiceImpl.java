package com.wangyang.bioinfo.service.impl;

import com.wangyang.bioinfo.pojo.authorize.User;
import com.wangyang.bioinfo.pojo.param.DataCategoryParam;
import com.wangyang.bioinfo.pojo.entity.DataCategory;
import com.wangyang.bioinfo.repository.DataCategoryRepository;
import com.wangyang.bioinfo.service.IDataCategoryService;
import com.wangyang.bioinfo.service.base.BaseTermServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

/**
 * @author wangyang
 * @date 2021/7/25
 */
@Service
@Transactional
public class DataCategoryServiceImpl extends BaseTermServiceImpl<DataCategory>
        implements IDataCategoryService {
    private final DataCategoryRepository dataCategoryRepository;
    public DataCategoryServiceImpl(DataCategoryRepository dataCategoryRepository) {
        super(dataCategoryRepository);
        this.dataCategoryRepository = dataCategoryRepository;
    }

    @Override
    public DataCategory add(DataCategoryParam dataCategoryParam, User user) {
        DataCategory dataCategory = new DataCategory();
        BeanUtils.copyProperties(dataCategoryParam,dataCategory);
        dataCategory.setUserId(user.getId());
        return dataCategoryRepository.save(dataCategory);
    }

    @Override
    public DataCategory update(Integer id, DataCategoryParam dataCategoryParam, User user) {
        DataCategory dataCategory = findById(id);
        BeanUtils.copyProperties(dataCategoryParam,dataCategory);
        dataCategory.setUserId(user.getId());
        return dataCategoryRepository.save(dataCategory);
    }
}
