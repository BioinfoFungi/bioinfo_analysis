package com.wangyang.bioinfo.service;

import com.github.rcaller.rstuff.RCaller;
import com.wangyang.bioinfo.pojo.Task;
import com.wangyang.bioinfo.pojo.file.Attachment;
import com.wangyang.bioinfo.pojo.file.CancerStudy;
import com.wangyang.bioinfo.pojo.file.Code;
import com.wangyang.bioinfo.service.base.IAbstractBaseFileService;
import org.springframework.scheduling.annotation.Async;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

/**
 * @author wangyang
 * @date 2021/7/22
 */
public interface ICodeService extends IAbstractBaseFileService<Code> {
//    void processByCancerStudyId(Integer cancerStudyId,  ServletOutputStream outputStream);

    void processAsyncByCancerStudy(Task task,CancerStudy cancerStudy);
}
