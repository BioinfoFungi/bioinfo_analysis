package com.wangyang.bioinfo.service.geo;

import com.wangyang.bioinfo.pojo.entity.geo.GSMSupport;
import com.wangyang.bioinfo.pojo.enums.CrudType;
import com.wangyang.bioinfo.repository.geo.GSMSupportRepository;
import com.wangyang.bioinfo.service.base.AbstractCrudService;
import org.springframework.stereotype.Service;

@Service
public class GSMSupportServiceImpl extends AbstractCrudService<GSMSupport,Integer> {

    private final GSMSupportRepository gsmSupportRepository;
    public GSMSupportServiceImpl(GSMSupportRepository gsmSupportRepository) {
        super(gsmSupportRepository);
        this.gsmSupportRepository = gsmSupportRepository;
    }

    @Override
    public boolean supportType(CrudType type) {
        return type.equals(CrudType.GSM);
    }
}
