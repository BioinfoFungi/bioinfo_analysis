package com.wangyang.bioinfo.service;

import com.wangyang.bioinfo.pojo.Task;
import com.wangyang.bioinfo.pojo.enums.TaskType;
import com.wangyang.bioinfo.service.base.ICrudService;

import java.util.List;

/**
 * @author wangyang
 * @date 2021/7/24
 */
public interface ITaskService  extends ICrudService<Task, Integer> {
    Task addTaskByCancerStudyId(Integer cancerStudyId);

    Task findByCodeAndObj(int codeId, int objId, TaskType taskType);

    Task addTask(Integer id, Integer cancerStudyId);

    Task runCancerStudyTask(int id);

    List<Task> listPending();

    List<Task> addTas2Queue();
}
