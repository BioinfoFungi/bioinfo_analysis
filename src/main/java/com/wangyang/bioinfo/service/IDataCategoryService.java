package com.wangyang.bioinfo.service;

import com.wangyang.bioinfo.pojo.authorize.User;
import com.wangyang.bioinfo.pojo.param.DataCategoryParam;
import com.wangyang.bioinfo.pojo.entity.DataCategory;
import com.wangyang.bioinfo.service.base.IBaseTermService;

/**
 * @author wangyang
 * @date 2021/7/25
 */
public interface IDataCategoryService extends IBaseTermService<DataCategory> {
    DataCategory add(DataCategoryParam dataCategoryParam, User user);

    DataCategory update(Integer id, DataCategoryParam dataCategoryParam, User user);
}
