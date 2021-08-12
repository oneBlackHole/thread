package com.szz.juc.pool;

import sun.misc.Unsafe;
import sun.rmi.runtime.NewThreadAction;

import java.util.concurrent.Callable;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.locks.LockSupport;

public class TaskFuture<T> implements Runnable,Future<T> {

    private AtomicInteger state = new AtomicInteger(0);

    private static final int NEW = 0;

    private static final int EXCEPTION = 1;

    private static final int FINISH = 2;

    private AtomicReference<Runnable> caller = new AtomicReference<>();

    private Callable task;

    public TaskFuture(Callable task) {
        this.task = task;
    }

    private T result;

    @Override
    public T get() {
        for (;;) {
            int s = state.get();
            if(s > NEW){//任务已处理
                return result;
            }
            //任务未处理需要阻塞结果线程
            if(caller.compareAndSet(null,Thread.currentThread())){
                LockSupport.park();
                finish();
                return result;
            }
        }

    }

    @Override
    public void run() {
        for (;;) {
            int s = state.get();
            if(s == NEW){//任务未处理
                try {
                    result = (T) task.call();
                    state.compareAndSet(NEW,FINISH);
                } catch (Exception e) {
                    result = (T) e;
                    state.compareAndSet(NEW,EXCEPTION);
                }
            }
            s = state.get();
            if(s > NEW){//处理完成唤醒结果线程
                if(caller.get() != null){
                    LockSupport.unpark((Thread) caller.get());
                }
                return;
            }

        }

    }

    private void finish(){
        caller.set(null);
    }

}
