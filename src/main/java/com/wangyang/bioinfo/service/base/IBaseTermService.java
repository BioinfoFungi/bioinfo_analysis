package com.wangyang.bioinfo.service.base;

import com.wangyang.bioinfo.pojo.base.BaseTerm;
import com.wangyang.bioinfo.pojo.param.BaseRNAParam;
import com.wangyang.bioinfo.pojo.param.BaseTermParam;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * @author wangyang
 * @date 2021/6/27
 */
public interface IBaseTermService<TERM extends BaseTerm>  extends ICrudService<TERM, Integer>{
    Page<TERM> pageBy(BaseTermParam baseTermParam, Pageable pageable);
}
