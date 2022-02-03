package com.wangyang.bioinfo.service.impl;

import com.wangyang.bioinfo.pojo.entity.CircRNA;
import com.wangyang.bioinfo.pojo.enums.CrudType;
import com.wangyang.bioinfo.repository.CircRNARepository;
import com.wangyang.bioinfo.service.ICircRNAService;
import com.wangyang.bioinfo.service.base.BaseRNAServiceImpl;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

/**
 * @author wangyang
 * @date 2021/7/10
 */
@Service
@Transactional
public class CircRNAServiceImpl
        extends BaseRNAServiceImpl<CircRNA>
        implements ICircRNAService {
    private final CircRNARepository circRNARepository;

    public CircRNAServiceImpl(CircRNARepository circRNARepository) {
        super(circRNARepository);
        this.circRNARepository = circRNARepository;
    }

    @Override
    public boolean supportType(CrudType type) {
        return false;
    }
}
