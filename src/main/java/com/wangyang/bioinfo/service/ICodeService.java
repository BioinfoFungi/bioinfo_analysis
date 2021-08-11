package com.wangyang.bioinfo.service;

import com.wangyang.bioinfo.pojo.Task;
import com.wangyang.bioinfo.pojo.dto.CodeMsg;
import com.wangyang.bioinfo.pojo.file.CancerStudy;
import com.wangyang.bioinfo.pojo.file.Code;
import com.wangyang.bioinfo.service.base.IBaseFileService;
import org.springframework.scheduling.annotation.Async;

import java.util.Map;

/**
 * @author wangyang
 * @date 2021/7/22
 */
public interface ICodeService extends IBaseFileService<Code> {
//    void processByCancerStudyId(Integer cancerStudyId,  ServletOutputStream outputStream);

    void processAsyncByCancerStudy(Task task,CancerStudy cancerStudy);

    void rCodePlot(Integer id, Integer cancerStudyId);


    void runRCode(Task task, Code code, CancerStudy cancerStudy);


    void rCodePlot(Integer Id, Map<String,String> maps);
}
