package com.wangyang.bioinfo.service.geo;

import com.wangyang.bioinfo.pojo.entity.geo.GPLSupport;
import com.wangyang.bioinfo.pojo.enums.CrudType;
import com.wangyang.bioinfo.repository.geo.GPLSupportRepository;
import com.wangyang.bioinfo.service.base.AbstractCrudService;
import org.springframework.stereotype.Service;

@Service
public class GPLSupportServiceImpl extends AbstractCrudService<GPLSupport,Integer> {

    private final  GPLSupportRepository gplSupportRepository;

    public GPLSupportServiceImpl(GPLSupportRepository gplSupportRepository) {
        super(gplSupportRepository);
        this.gplSupportRepository=gplSupportRepository;
    }
//    private final GPLSupportRepository gplSupportRepository;



    @Override
    public boolean supportType(CrudType type) {
        return type.equals(CrudType.GPL);
    }
}
