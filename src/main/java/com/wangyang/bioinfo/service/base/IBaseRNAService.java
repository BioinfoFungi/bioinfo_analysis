package com.wangyang.bioinfo.service.base;

import com.wangyang.bioinfo.pojo.base.BaseRNA;
import com.wangyang.bioinfo.pojo.param.BaseRNAParam;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


/**
 * @author wangyang
 * @date 2021/6/27
 */
public interface IBaseRNAService<T extends BaseRNA> extends ICrudService<T, Integer> {

    T findByName(String name);

    Page<T> pageBy(BaseRNAParam baseRNAParam, Pageable pageable);
}
