package com.wangyang.bioinfo.service.base;

import com.wangyang.bioinfo.pojo.base.BaseRNA;
import com.wangyang.bioinfo.pojo.param.BaseRNAQuery;
import com.wangyang.bioinfo.pojo.vo.RNAVO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;


/**
 * @author wangyang
 * @date 2021/6/27
 */
public interface IBaseRNAService<T extends BaseRNA> extends ICrudService<T, Integer> {


    T findByGeneId(String geneId);

    T findByName(String name);

    Page<T> pageBy(BaseRNAQuery baseRNAQuery, Pageable pageable);


    Page<RNAVO> convert(Page<T> page);
}
