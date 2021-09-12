package com.wangyang.bioinfo.service.impl;

import com.wangyang.bioinfo.pojo.authorize.User;
import com.wangyang.bioinfo.pojo.param.AnalysisSoftwareParam;
import com.wangyang.bioinfo.pojo.entity.AnalysisSoftware;
import com.wangyang.bioinfo.repository.AnalysisSoftwareRepository;
import com.wangyang.bioinfo.service.IAnalysisSoftwareService;
import com.wangyang.bioinfo.service.base.BaseTermServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

/**
 * @author wangyang
 * @date 2021/7/25
 */
@Service
@Transactional
public class AnalysisSoftwareServiceImpl extends BaseTermServiceImpl<AnalysisSoftware>
        implements IAnalysisSoftwareService {
    private  final  AnalysisSoftwareRepository analysisSoftwareRepository;
    public AnalysisSoftwareServiceImpl(AnalysisSoftwareRepository analysisSoftwareRepository) {
        super(analysisSoftwareRepository);
        this.analysisSoftwareRepository = analysisSoftwareRepository;
    }

    @Override
    public AnalysisSoftware add(AnalysisSoftwareParam analysisSoftwareParam, User user) {
        AnalysisSoftware analysisSoftware = new AnalysisSoftware();
        BeanUtils.copyProperties(analysisSoftwareParam,analysisSoftware);
        analysisSoftware.setUserId(user.getId());
        return analysisSoftwareRepository.save(analysisSoftware);
    }

    @Override
    public AnalysisSoftware update(Integer id, AnalysisSoftwareParam analysisSoftwareParam, User user) {
        AnalysisSoftware analysisSoftware = findById(id);
        BeanUtils.copyProperties(analysisSoftwareParam,analysisSoftware);
        analysisSoftware.setUserId(user.getId());
        return analysisSoftwareRepository.save(analysisSoftware);
    }

    //    private final AnalysisSoftwareRepository analysisSoftwareRepository;
//    public AnalysisSoftwareServiceImpl(AnalysisSoftwareRepository AnalysisSoftwareRepository) {
//        super(analysisSoftwareRepository);
//        this.AnalysisSoftwareRepository= analysisSoftwareRepository;
//    }
}
