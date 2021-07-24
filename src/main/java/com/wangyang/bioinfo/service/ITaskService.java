package com.wangyang.bioinfo.service;

import com.wangyang.bioinfo.pojo.Task;
import com.wangyang.bioinfo.service.base.ICrudService;

import java.util.List;

/**
 * @author wangyang
 * @date 2021/7/24
 */
public interface ITaskService  extends ICrudService<Task, Integer> {
    Task addTaskByCancerStudyId(Integer cancerStudyId);

    List<Task> listPending();

    List<Task> addTas2Queue();
}
