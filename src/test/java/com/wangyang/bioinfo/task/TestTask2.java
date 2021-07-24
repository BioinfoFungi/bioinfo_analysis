package com.wangyang.bioinfo.task;

import com.wangyang.bioinfo.task.manage2.MyTask;
import com.wangyang.bioinfo.task.manage2.TaskPoolQueue;

/**
 * @author wangyang
 * @date 2021/7/24
 */
public class TestTask2 {

    public void test1(){
        for (int i = 1; i <= 10; i++) {
            MyTask task = new MyTask(String.valueOf(i));
            TaskPoolQueue.execute(task);
        }
    }

    public static void main(String[] args) {
        new TestTask2().test1();
    }
}
