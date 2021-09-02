package com.wangyang.bioinfo.service;

import com.wangyang.bioinfo.handle.ICodeResult;
import com.wangyang.bioinfo.pojo.Task;
import com.wangyang.bioinfo.pojo.authorize.User;
import com.wangyang.bioinfo.pojo.base.BaseFile;
import com.wangyang.bioinfo.pojo.file.CancerStudy;
import com.wangyang.bioinfo.pojo.file.Code;
import org.springframework.scheduling.annotation.Async;

import java.util.Map;

public interface IAsyncService  {
    void processCancerStudy1(User user,Task task, Code code,BaseFile baseFile, ICodeResult<? extends BaseFile> codeResult);

    @Async("taskExecutor")
    void processCancerStudy2(User user,Task task, Code code,CancerStudy cancerStudy, CancerStudy cancerStudyProcess ,Map<String, Object> map);

    Task shutdownProcess(int taskId);

}
