package com.wangyang.bioinfo.service;

import com.wangyang.bioinfo.pojo.Task;
import com.wangyang.bioinfo.pojo.User;
import com.wangyang.bioinfo.pojo.file.CancerStudy;
import com.wangyang.bioinfo.pojo.file.Code;
import com.wangyang.bioinfo.pojo.vo.CancerStudyVO;
import com.wangyang.bioinfo.pojo.vo.TermMappingVo;
import org.springframework.scheduling.annotation.Async;

import java.util.Map;

public interface IAsyncService  {
    @Async("taskExecutor")
    void processCancerStudy(Task task, Code code, CancerStudy cancerStudyProcess ,Map<String, Object> map);
}
