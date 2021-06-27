package com.wangyang.bioinfo.service.base;

import com.wangyang.bioinfo.pojo.base.BaseTerm;
import com.wangyang.bioinfo.repository.base.BaseTermRepository;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author wangyang
 * @date 2021/6/27
 */
public class BaseTermServiceImpl<TERM extends BaseTerm>
        extends AbstractCrudService<TERM,Integer>
        implements IBaseTermService<TERM> {

    @Autowired
    BaseTermRepository<TERM> baseTermRepository;
}
