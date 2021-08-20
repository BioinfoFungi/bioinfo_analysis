package com.wangyang.bioinfo.service.base;

import com.wangyang.bioinfo.pojo.file.BaseDataCategory;
import com.wangyang.bioinfo.pojo.file.CancerStudy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * @author wangyang
 * @date 2021/7/25
 */
public interface IBaseDataCategoryService<CATEGORY extends BaseDataCategory> extends IBaseFileService<CATEGORY>{
    Page<CATEGORY> pageBy(CancerStudy cancerStudy, String keyWards, Pageable pageable);

    List<CATEGORY> findDataByCategoryId(CancerStudy cancerStudy,String keyWards);
}
