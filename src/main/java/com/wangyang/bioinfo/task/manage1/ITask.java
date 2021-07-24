package com.wangyang.bioinfo.task.manage1;

/**
 * @author wangyang
 * @date 2021/7/24
 * https://www.cnblogs.com/ganchuanpu/p/9115239.html
 */
public interface ITask extends Comparable<ITask>{
    void run();
    void setPriority(Priority priority);
    Priority getPriority();
    void setSequence(int sequence);
    int getSequence();
}
