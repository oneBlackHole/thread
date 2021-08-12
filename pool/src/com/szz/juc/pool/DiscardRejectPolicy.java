package com.szz.juc.pool;

public class DiscardRejectPolicy implements RejectPolicy{
    @Override
    public void reject(Runnable task, MyTheadPoolExecutor myTheadPoolExecutor) {
        //do nothing
        System.out.println("discard one task");
    }
}
