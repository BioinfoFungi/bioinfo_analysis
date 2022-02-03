package com.wangyang.bioinfo.service.impl;

import com.wangyang.bioinfo.pojo.entity.LncRNA;
import com.wangyang.bioinfo.pojo.enums.CrudType;
import com.wangyang.bioinfo.repository.LncRNARepository;
import com.wangyang.bioinfo.service.ILncRNAService;
import com.wangyang.bioinfo.service.base.BaseRNAServiceImpl;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

/**
 * @author wangyang
 * @date 2021/6/27
 */
@Service
@Transactional
public class LncRNAServiceImpl extends BaseRNAServiceImpl<LncRNA> implements ILncRNAService {
    private  final LncRNARepository lncRNARepository;

    public LncRNAServiceImpl(LncRNARepository lncRNARepository) {
        super(lncRNARepository);
        this.lncRNARepository = lncRNARepository;
    }


//    public LncRNAServiceImpl(BaseRNARepository<LncRNA> baseRNARepository) {
//        super(baseRNARepository);
//    }


    @Override
    public boolean supportType(CrudType type) {
        return false;
    }
}
