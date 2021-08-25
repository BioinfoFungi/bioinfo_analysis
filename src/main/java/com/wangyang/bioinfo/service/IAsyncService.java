package com.wangyang.bioinfo.service;

import com.wangyang.bioinfo.pojo.Task;
import com.wangyang.bioinfo.pojo.file.CancerStudy;
import com.wangyang.bioinfo.pojo.file.Code;
import org.springframework.scheduling.annotation.Async;

import java.util.Map;

public interface IAsyncService  {
    void processCancerStudy1(Task task, Code code, CancerStudy cancerStudy, CancerStudy cancerStudyProcess, Map<String, Object> map);

    @Async("taskExecutor")
    void processCancerStudy2(Task task, Code code,CancerStudy cancerStudy, CancerStudy cancerStudyProcess ,Map<String, Object> map);

    Task shutdownProcess(int taskId);
}
