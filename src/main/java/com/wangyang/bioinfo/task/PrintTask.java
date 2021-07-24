package com.wangyang.bioinfo.task;

/**
 * @author wangyang
 * @date 2021/7/24
 */
public class PrintTask  implements ITask{
    // 默认优先级。
    private Priority priority = Priority.DEFAULT;
    private int id;
    private int sequence;

    public PrintTask(int id) {
        this.id = id;
    }

    @Override
    public void run() {
        // 为了尽量模拟窗口办事的速度，我们这里停顿两秒。
        try {
            Thread.sleep(2000);
        } catch (InterruptedException ignored) {
        }

        System.out.println("我的id是：" + id);
    }

    @Override
    public void setPriority(Priority priority) {
        this.priority = priority;
    }

    @Override
    public Priority getPriority() {
        return priority;
    }

    @Override
    public void setSequence(int sequence) {
        this.sequence = sequence;
    }

    @Override
    public int getSequence() {
        return sequence;
    }

    // 做优先级比较。
    @Override
    public int compareTo(ITask another) {
        final Priority me = this.getPriority();
        final Priority it = another.getPriority();
        return me == it ?  this.getSequence() - another.getSequence() : it.ordinal() - me.ordinal();
    }

}
