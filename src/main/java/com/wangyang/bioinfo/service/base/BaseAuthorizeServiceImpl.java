package com.wangyang.bioinfo.service.base;

import com.wangyang.bioinfo.pojo.authorize.BaseAuthorize;
import com.wangyang.bioinfo.repository.base.BaseAuthorizeRepository;

public class BaseAuthorizeServiceImpl<AUTHORIZE extends BaseAuthorize>  extends AbstractCrudService<AUTHORIZE,Integer>
        implements IBaseAuthorizeService<AUTHORIZE> {

    BaseAuthorizeRepository<AUTHORIZE> baseAuthorizeRepository;
}
