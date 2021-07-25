package com.wangyang.bioinfo.service;

import com.wangyang.bioinfo.pojo.Task;
import com.wangyang.bioinfo.pojo.file.CancerStudy;
import com.wangyang.bioinfo.pojo.file.Code;
import com.wangyang.bioinfo.service.base.IBaseFileService;

/**
 * @author wangyang
 * @date 2021/7/22
 */
public interface ICodeService extends IBaseFileService<Code> {
//    void processByCancerStudyId(Integer cancerStudyId,  ServletOutputStream outputStream);

    void processAsyncByCancerStudy(Task task,CancerStudy cancerStudy);
}
