package com.szz.juc.MyThreadPool;

public interface Executor {
    void execute(Runnable command);
}
