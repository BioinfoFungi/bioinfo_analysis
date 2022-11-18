package com.wangyang.bioinfo.service.task;

import com.wangyang.bioinfo.pojo.entity.base.BaseEntity;
import com.wangyang.bioinfo.service.base.ICrudService;
import com.wangyang.bioinfo.task.ICodeResult;
import com.wangyang.bioinfo.pojo.entity.Task;
import com.wangyang.bioinfo.pojo.authorize.User;
import com.wangyang.bioinfo.pojo.entity.base.BaseFile;
import com.wangyang.bioinfo.pojo.entity.CancerStudy;
import com.wangyang.bioinfo.pojo.entity.Code;
import org.springframework.scheduling.annotation.Async;

import java.util.Map;

public interface IAsyncService  {
//    void processCancerStudy1(User user, Task task, Code code, BaseFile baseFile, ICodeResult<? extends BaseFile> codeResult);

    @Async("taskExecutor")
    void processCancerStudy2(User user,  Task task,  Map<String, String> map, Code code);

    Task shutdownProcess(int taskId);

    void processCancerStudy1(User user,  Task task,  Map<String, String> map, Code code);
}
