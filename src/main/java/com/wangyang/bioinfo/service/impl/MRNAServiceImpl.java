package com.wangyang.bioinfo.service.impl;

import com.wangyang.bioinfo.pojo.MRNA;
import com.wangyang.bioinfo.repository.MRNARepository;
import com.wangyang.bioinfo.service.IMRNAService;
import com.wangyang.bioinfo.service.base.BaseRNAServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author wangyang
 * @date 2021/6/27
 */
@Service
public class MRNAServiceImpl extends BaseRNAServiceImpl<MRNA> implements IMRNAService {
    @Autowired
    MRNARepository mrnaRepository;
}
