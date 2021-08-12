package com.szz.juc.pool;

public class CallerRunRejectPolicy implements RejectPolicy{
    @Override
    public void reject(Runnable task, MyTheadPoolExecutor myTheadPoolExecutor) {
        //自己运行
        System.out.println("caller run task");
        task.run();
    }
}
