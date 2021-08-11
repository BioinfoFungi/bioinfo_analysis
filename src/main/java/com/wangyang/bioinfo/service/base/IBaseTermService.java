package com.wangyang.bioinfo.service.base;

import com.wangyang.bioinfo.pojo.base.BaseTerm;
import com.wangyang.bioinfo.pojo.param.BaseTermParam;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Collection;
import java.util.List;

/**
 * @author wangyang
 * @date 2021/6/27
 */
public interface IBaseTermService<TERM extends BaseTerm>  extends ICrudService<TERM, Integer>{
    Page<TERM> pageBy(BaseTermParam baseTermParam, Pageable pageable);

    TERM findByEnName(String name);

    TERM findAndCheckByEnName(String name);

    TERM save(BaseTermParam baseTermParam);

    List<TERM> findAllById(Collection<Integer> id);
}
