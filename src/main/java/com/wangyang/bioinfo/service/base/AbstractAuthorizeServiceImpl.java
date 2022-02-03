package com.wangyang.bioinfo.service.base;

import com.wangyang.bioinfo.pojo.authorize.BaseAuthorize;
import com.wangyang.bioinfo.repository.base.BaseAuthorizeRepository;
import com.wangyang.bioinfo.repository.base.BaseRepository;

public abstract class AbstractAuthorizeServiceImpl<AUTHORIZE extends BaseAuthorize>
        extends AbstractCrudService<AUTHORIZE,Integer>
        implements IBaseAuthorizeService<AUTHORIZE> {

    private final BaseAuthorizeRepository<AUTHORIZE> baseAuthorizeService;
    public AbstractAuthorizeServiceImpl(BaseAuthorizeRepository<AUTHORIZE> baseAuthorizeService) {
        super(baseAuthorizeService);
        this.baseAuthorizeService =baseAuthorizeService;
    }
}
