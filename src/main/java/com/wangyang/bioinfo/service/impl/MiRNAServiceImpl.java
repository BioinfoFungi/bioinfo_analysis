package com.wangyang.bioinfo.service.impl;

import com.wangyang.bioinfo.pojo.entity.MiRNA;
import com.wangyang.bioinfo.repository.MiRNARepository;
import com.wangyang.bioinfo.service.IMiRNAService;
import com.wangyang.bioinfo.service.base.BaseRNAServiceImpl;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

/**
 * @author wangyang
 * @date 2021/6/27
 */
@Service
@Transactional
public class MiRNAServiceImpl extends BaseRNAServiceImpl<MiRNA> implements IMiRNAService {

    private final MiRNARepository miRNARepository;

    public MiRNAServiceImpl(MiRNARepository miRNARepository) {
        super(miRNARepository);
        this.miRNARepository =miRNARepository;
    }
}
