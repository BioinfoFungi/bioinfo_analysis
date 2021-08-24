package com.wangyang.bioinfo.pojo.dto;

import com.wangyang.bioinfo.pojo.Task;
import lombok.Data;

@Data
public class TaskProcess {
    Task task;
    Process process;

    public TaskProcess(Task task, Process process) {
        this.task = task;
        this.process = process;
    }
}
