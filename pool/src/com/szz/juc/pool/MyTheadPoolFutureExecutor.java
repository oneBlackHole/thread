package com.szz.juc.pool;


import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;

public class MyTheadPoolFutureExecutor extends MyTheadPoolExecutor{
    public MyTheadPoolFutureExecutor(String name, int coreSize, int maxSize, BlockingQueue<Runnable> taskDueue, RejectPolicy rejectPolicy) {
        super(name, coreSize, maxSize, taskDueue, rejectPolicy);
    }

    public <T> TaskFuture<T> submit(Callable task) {
        TaskFuture<T> taskFuture = new TaskFuture<>(task);
        execute(taskFuture);
        return taskFuture;
    }

}
