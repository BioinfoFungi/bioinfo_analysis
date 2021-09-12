package com.wangyang.bioinfo.service.impl;

import com.wangyang.bioinfo.pojo.entity.MRNA;
import com.wangyang.bioinfo.repository.MRNARepository;
import com.wangyang.bioinfo.service.IMRNAService;
import com.wangyang.bioinfo.service.base.BaseRNAServiceImpl;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

/**
 * @author wangyang
 * @date 2021/6/27
 */
@Service
@Transactional
public class MRNAServiceImpl extends BaseRNAServiceImpl<MRNA> implements IMRNAService {

    private final MRNARepository mrnaRepository;


    public MRNAServiceImpl( MRNARepository   mrnaRepository) {
        super(mrnaRepository);
        this.mrnaRepository=mrnaRepository;
    }
}
