package com.wangyang.bioinfo.service.impl;

import com.wangyang.bioinfo.pojo.RNA.LncRNA;
import com.wangyang.bioinfo.repository.LncRNARepository;
import com.wangyang.bioinfo.repository.base.BaseRNARepository;
import com.wangyang.bioinfo.service.ILncRNAService;
import com.wangyang.bioinfo.service.base.BaseRNAServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author wangyang
 * @date 2021/6/27
 */
@Service
public class LncRNAServiceImpl extends BaseRNAServiceImpl<LncRNA> implements ILncRNAService {
    private  final LncRNARepository lncRNARepository;

    public LncRNAServiceImpl(LncRNARepository lncRNARepository) {
        super(lncRNARepository);
        this.lncRNARepository = lncRNARepository;
    }


//    public LncRNAServiceImpl(BaseRNARepository<LncRNA> baseRNARepository) {
//        super(baseRNARepository);
//    }
}
