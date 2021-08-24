package com.wangyang.bioinfo.service;

import com.wangyang.bioinfo.pojo.Task;
import com.wangyang.bioinfo.pojo.User;
import com.wangyang.bioinfo.pojo.param.TaskParam;
import com.wangyang.bioinfo.pojo.param.TaskQuery;
import com.wangyang.bioinfo.service.base.ICrudService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * @author wangyang
 * @date 2021/7/24
 */
public interface ITaskService  extends ICrudService<Task, Integer> {

    Page<Task> page(TaskQuery taskQuery, Pageable pageable);

    List<Task> findByCodeId(Integer codeId);

    List<Task> delByCodeId(Integer codeId);

    List<Task> findByCanSId(Integer canSId);

    List<Task> delByCanSId(Integer canSId);

    Task addTask(TaskParam taskParam, User user);

    Task shutdownProcess(int taskId);

    Task runTask(Integer id, User user);
//    Task addTaskByCancerStudyId(Integer cancerStudyId);
//
//    Task findByCodeAndObj(int codeId, int objId, TaskType taskType);
//
//    Task addTask(Integer id, Integer cancerStudyId);
//
//    Task runCancerStudyTask(int id);

    List<Task> listPending();

    List<Task> addTas2Queue();


}
