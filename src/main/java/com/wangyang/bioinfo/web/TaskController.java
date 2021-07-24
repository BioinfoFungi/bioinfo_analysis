package com.wangyang.bioinfo.web;

import com.github.rcaller.rstuff.RCaller;
import com.wangyang.bioinfo.handle.SpringWebSocketHandler;
import com.wangyang.bioinfo.pojo.Task;
import com.wangyang.bioinfo.pojo.file.CancerStudy;
import com.wangyang.bioinfo.service.ICodeService;
import com.wangyang.bioinfo.service.ITaskService;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.socket.TextMessage;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.concurrent.Executor;

/**
 * @author wangyang
 * @date 2021/7/22
 */
@RestController
@RequestMapping("/api/task")
public class TaskController {

    @Autowired
    ITaskService taskService;

    @GetMapping("/add/{cancerStudyId}")
    public Task addTask(@PathVariable("cancerStudyId") Integer cancerStudyId) {
        Task task = taskService.addTaskByCancerStudyId(cancerStudyId);
        return task;
    }
}
