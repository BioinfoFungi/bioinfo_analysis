package com.wangyang.bioinfo.web;

import com.wangyang.bioinfo.pojo.Task;
import com.wangyang.bioinfo.pojo.User;
import com.wangyang.bioinfo.pojo.param.TaskParam;
import com.wangyang.bioinfo.pojo.param.TaskQuery;
import com.wangyang.bioinfo.service.ITaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

import static org.springframework.data.domain.Sort.Direction.DESC;

/**
 * @author wangyang
 * @date 2021/7/22
 */
@RestController
@RequestMapping("/api/task")
public class TaskController {
    //http://localhost:8080/api/task?codeId=1&cancerStudyId=66&authorize=
    //ws://localhost:8080/websocket/socketServer.do
    //http://coolaf.com/tool/chattest
    @Autowired
    ITaskService taskService;

//    @GetMapping("/add/{cancerStudyId}")
//    public Task addTask(@PathVariable("cancerStudyId") Integer cancerStudyId) {
//        Task task = taskService.addTaskByCancerStudyId(cancerStudyId);
//        return task;
//    }

    @GetMapping("/run")
    public Task addTask(TaskParam taskParam, HttpServletRequest request){
        User user = (User) request.getAttribute("user");
        Task task = taskService.addTask(taskParam,user);
        return task;
    }

    @GetMapping("/run/{id}")
    public Task addTask(@PathVariable("id") Integer id, HttpServletRequest request){
        User user = (User) request.getAttribute("user");
        Task task = taskService.runTask(id,user);
        return task;
    }
    @GetMapping("/shutdown/{taskId}")
    public Task shutdownProcess(@PathVariable("taskId") int taskId){
        Task task = taskService.shutdownProcess(taskId);
        return task;
    }
    @GetMapping
    public Page<Task> page(TaskQuery taskQuery, @PageableDefault(sort = {"id"},direction = DESC) Pageable pageable){
        return taskService.page(taskQuery,pageable);
    }
    @GetMapping("/del/{id}")
    public Task del(@PathVariable("id") Integer id){
        return  taskService.delBy(id);
    }
}
