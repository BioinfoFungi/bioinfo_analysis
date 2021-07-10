package com.wangyang.bioinfo.service.impl;

import com.wangyang.bioinfo.pojo.RNA.CircRNA;
import com.wangyang.bioinfo.repository.CircRNARepository;
import com.wangyang.bioinfo.service.ICircRNAService;
import com.wangyang.bioinfo.service.base.BaseRNAServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author wangyang
 * @date 2021/7/10
 */
@Service
public class CircRNAServiceImpl
        extends BaseRNAServiceImpl<CircRNA>
        implements ICircRNAService {
    @Autowired
    CircRNARepository circRNARepository;
}
