package com.wangyang.bioinfo.service.geo;

import com.wangyang.bioinfo.handle.FileHandlers;
import com.wangyang.bioinfo.pojo.entity.geo.GSESupport;
import com.wangyang.bioinfo.pojo.enums.CrudType;
import com.wangyang.bioinfo.repository.geo.GSESupportRepository;
import com.wangyang.bioinfo.service.*;
import com.wangyang.bioinfo.service.base.AbstractTermMappingService;
import org.springframework.stereotype.Service;

@Service
public class GSESupportServiceImpl extends AbstractTermMappingService<GSESupport> {

    private final GSESupportRepository geoSupportRepository;
    public GSESupportServiceImpl(FileHandlers fileHandlers,
                                 GSESupportRepository geoSupportRepository,
                                 ICancerService cancerService,
                                 IStudyService studyService,
                                 IDataOriginService dataOriginService,
                                 IDataCategoryService dataCategoryService,
                                 IAnalysisSoftwareService analysisSoftwareService) {

        super(fileHandlers, geoSupportRepository, cancerService, studyService, dataOriginService, dataCategoryService, analysisSoftwareService);
        this.geoSupportRepository = geoSupportRepository;
    }

    @Override
    public boolean supportType(CrudType type) {
        return type.equals(CrudType.GSE);
    }
}
