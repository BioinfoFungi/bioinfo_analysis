package com.wangyang.bioinfo.task;

import org.junit.jupiter.api.Test;

/**
 * @author wangyang
 * @date 2021/7/24
 */
public class TestTask {

    @Test
    public void task1(){
        // 这里暂时只开一个窗口。
        TaskQueue taskQueue = new TaskQueue(1);
        taskQueue.start();//打开窗口

        for (int i = 0; i < 10; i++) {
            PrintTask task = new PrintTask(i);
            taskQueue.add(task);
        }
    }

    public static void main(String[] args) {
        new TestTask().task1();
    }
}
