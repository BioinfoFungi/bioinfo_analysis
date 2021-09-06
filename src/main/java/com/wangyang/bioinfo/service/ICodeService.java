package com.wangyang.bioinfo.service;

import com.wangyang.bioinfo.pojo.authorize.User;
import com.wangyang.bioinfo.pojo.enums.CodeType;
import com.wangyang.bioinfo.pojo.file.Code;
import com.wangyang.bioinfo.pojo.param.CodeParam;
import com.wangyang.bioinfo.pojo.param.CodeQuery;
import com.wangyang.bioinfo.pojo.support.FileTree;
import com.wangyang.bioinfo.pojo.vo.CodeVO;
import com.wangyang.bioinfo.service.base.ITermMappingService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author wangyang
 * @date 2021/7/22
 */

public interface ICodeService extends ITermMappingService<Code> {

    Page<Code> pageBy(CodeQuery codeQuery, Pageable pageable);

    Page<CodeVO> convertVo(Page<Code> codes);

    Code saveBy(CodeParam codeParam, User user);

    Code updateBy(Integer id, CodeParam codeParam, User user);

    List<Code> findByCan(Integer id);


    List<Code> listAllAnnTask();

    CodeType checkCodeType(String path);


//    void processByCancerStudyId(Integer cancerStudyId,  ServletOutputStream outputStream);

//    void processAsyncByCancerStudy(Task task,CancerStudy cancerStudy);

//    void rCodePlot(Integer id, Integer cancerStudyId);


//    void runRCode(Task task, Code code, CancerStudy cancerStudy);
//
//
//    void rCodePlot(Integer Id, Map<String,String> maps);
}
