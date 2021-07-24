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
        TaskQueue taskQueue = new TaskQueue(5);


        for (int i = 0; i < 10; i++) {
            PrintTask task = new PrintTask(i);
            taskQueue.add(task);
        }
        taskQueue.start();//打开窗口
    }
    public void task2() {
        // 开一个窗口，这样会让优先级更加明显。
        TaskQueue taskQueue = new TaskQueue(1);
        taskQueue.start(); //  // 某机构开始工作。

        // 为了显示出优先级效果，我们预添加3个在前面堵着，让后面的优先级效果更明显。
        taskQueue.add(new PrintTask(110));
        taskQueue.add(new PrintTask(112));
        taskQueue.add(new PrintTask(122));

        for (int i = 0; i < 10; i++) { // 从第0个人开始。
            PrintTask task = new PrintTask(i);
            if (1 == i) {
                task.setPriority(Priority.LOW); // 让第2个进入的人最后办事。
            } else if (8 == i) {
                task.setPriority(Priority.HIGH); // 让第9个进入的人第二个办事。
            } else if (9 == i) {
                task.setPriority(Priority.Immediately); // 让第10个进入的人第一个办事。
            }
            // ... 其它进入的人，按照进入顺序办事。
            taskQueue.add(task);
        }
    }
    public static void main(String[] args) {
        new TestTask().task2();
    }
}
