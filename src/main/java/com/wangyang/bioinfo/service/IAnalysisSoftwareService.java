package com.wangyang.bioinfo.service;

import com.wangyang.bioinfo.pojo.authorize.User;
import com.wangyang.bioinfo.pojo.param.AnalysisSoftwareParam;
import com.wangyang.bioinfo.pojo.entity.AnalysisSoftware;
import com.wangyang.bioinfo.service.base.IBaseTermService;

/**
 * @author wangyang
 * @date 2021/7/25
 */
public interface IAnalysisSoftwareService extends IBaseTermService<AnalysisSoftware> {
    AnalysisSoftware add(AnalysisSoftwareParam analysisSoftwareParam, User user);

    AnalysisSoftware update(Integer id, AnalysisSoftwareParam analysisSoftwareParam, User user);
}
