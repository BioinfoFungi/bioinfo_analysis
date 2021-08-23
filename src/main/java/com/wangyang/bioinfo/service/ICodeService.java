package com.wangyang.bioinfo.service;

import com.wangyang.bioinfo.pojo.Task;
import com.wangyang.bioinfo.pojo.User;
import com.wangyang.bioinfo.pojo.dto.CodeMsg;
import com.wangyang.bioinfo.pojo.file.CancerStudy;
import com.wangyang.bioinfo.pojo.file.Code;
import com.wangyang.bioinfo.pojo.param.CodeParam;
import com.wangyang.bioinfo.pojo.param.CodeQuery;
import com.wangyang.bioinfo.service.base.IBaseDataCategoryService;
import com.wangyang.bioinfo.service.base.IBaseFileService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @author wangyang
 * @date 2021/7/22
 */

public interface ICodeService extends IBaseDataCategoryService<Code> {
    Code findBy(String dataCategory,String analysisSoftware);

    Page<Code> pageBy(CodeQuery codeQuery, Pageable pageable);

    Code saveBy(CodeParam codeParam, User user);

    Code updateBy(Integer id, CodeParam codeParam, User user);

    List<Code> findByCan(Integer id);

//    void processByCancerStudyId(Integer cancerStudyId,  ServletOutputStream outputStream);

//    void processAsyncByCancerStudy(Task task,CancerStudy cancerStudy);

//    void rCodePlot(Integer id, Integer cancerStudyId);


//    void runRCode(Task task, Code code, CancerStudy cancerStudy);
//
//
//    void rCodePlot(Integer Id, Map<String,String> maps);
}
