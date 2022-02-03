package com.wangyang.bioinfo.service.impl;

import com.wangyang.bioinfo.pojo.entity.Sample;
import com.wangyang.bioinfo.pojo.enums.CrudType;
import com.wangyang.bioinfo.repository.SampleRepository;
import com.wangyang.bioinfo.service.ISampleService;
import com.wangyang.bioinfo.service.base.AbstractCrudService;
import org.springframework.stereotype.Service;

/**
 * @author wangyang
 * @date 2021/6/28
 */
@Service
public class SampleServiceImpl extends AbstractCrudService<Sample,Integer> implements ISampleService {
    private final SampleRepository sampleRepository;
    public SampleServiceImpl(SampleRepository sampleRepository) {
        super(sampleRepository);
        this.sampleRepository=sampleRepository;
    }

    @Override
    public boolean supportType(CrudType type) {
        return false;
    }
}
