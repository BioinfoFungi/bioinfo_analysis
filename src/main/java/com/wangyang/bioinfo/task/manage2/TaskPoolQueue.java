package com.wangyang.bioinfo.task.manage2;

import com.wangyang.bioinfo.handle.MyIgnorePolicy;
import com.wangyang.bioinfo.handle.NameTreadFactory;

import java.util.concurrent.*;

/**
 * @author wangyang
 * @date 2021/7/24
 */
public class TaskPoolQueue {

    private  static ThreadPoolExecutor pool =null;
    private static ThreadPoolExecutor getInstance(){
        if(pool==null){
            int corePoolSize = 2;
            int maximumPoolSize = 4;
            long keepAliveTime = 10;
            TimeUnit unit = TimeUnit.SECONDS;
            BlockingQueue<Runnable> workQueue = new ArrayBlockingQueue<>(2);
            ThreadFactory threadFactory = new NameTreadFactory();
            RejectedExecutionHandler handler = new MyIgnorePolicy();
            pool = new ThreadPoolExecutor(corePoolSize, maximumPoolSize, keepAliveTime, unit,
                    workQueue, threadFactory, handler);
        }
        pool.prestartAllCoreThreads();
        return pool;
    }


    public static void execute(Runnable runnable){
        getInstance().execute(runnable);
    }
}
