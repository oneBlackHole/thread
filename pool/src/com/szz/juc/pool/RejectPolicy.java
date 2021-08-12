package com.szz.juc.pool;

public interface RejectPolicy{
    void reject(Runnable task,MyTheadPoolExecutor myTheadPoolExecutor);
}
