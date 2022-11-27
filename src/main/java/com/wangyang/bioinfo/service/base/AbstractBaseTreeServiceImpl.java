package com.wangyang.bioinfo.service.base;

import com.wangyang.bioinfo.pojo.entity.base.BaseRNA;
import com.wangyang.bioinfo.pojo.entity.base.BaseTree;
import com.wangyang.bioinfo.repository.base.BaseRepository;
import com.wangyang.bioinfo.repository.base.BaseTreeRepository;

public abstract class AbstractBaseTreeServiceImpl <BASETREE extends BaseTree> extends AbstractCrudService<BASETREE,Integer> implements IBaseTreeService<BASETREE>{

    private BaseTreeRepository<BaseTree> baseTreeRepository;
    public AbstractBaseTreeServiceImpl(BaseTreeRepository baseTreeRepository) {
        super(baseTreeRepository);
        this.baseTreeRepository =baseTreeRepository;
    }
}
