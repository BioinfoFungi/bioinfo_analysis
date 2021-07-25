package com.wangyang.bioinfo.service.base;

import com.wangyang.bioinfo.pojo.dto.DataCategoryIdDto;
import com.wangyang.bioinfo.pojo.file.BaseDataCategory;

import java.util.List;

/**
 * @author wangyang
 * @date 2021/7/25
 */
public interface IBaseDataCategoryService<CATEGORY extends BaseDataCategory> extends IBaseFileService<CATEGORY>{
    List<CATEGORY> findDataByCategoryId(DataCategoryIdDto dataCategoryId);
}
