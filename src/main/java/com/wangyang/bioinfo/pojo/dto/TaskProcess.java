package com.wangyang.bioinfo.pojo.dto;

import com.wangyang.bioinfo.pojo.Task;
import lombok.Data;

@Data
public class TaskProcess {
    Task task;
    Process process;
    Runnable runnable;

    public TaskProcess(Task task,Runnable runnable, Process process) {
        this.task = task;
        this.process = process;
        this.runnable = runnable;
    }
}
