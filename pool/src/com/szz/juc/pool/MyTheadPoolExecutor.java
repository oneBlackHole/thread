package com.szz.juc.pool;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

public class MyTheadPoolExecutor implements Exceutor{

    private String name;

    private AtomicInteger sequence = new AtomicInteger(0);

    private int coreSize;

    private int maxSize;

    private BlockingQueue<Runnable> taskDueue;

    private RejectPolicy rejectPolicy;

    private AtomicInteger runningCount = new AtomicInteger(0);

    public MyTheadPoolExecutor(String name, int coreSize, int maxSize, BlockingQueue<Runnable> taskDueue, RejectPolicy rejectPolicy) {
        this.name = name;
        this.coreSize = coreSize;
        this.maxSize = maxSize;
        this.taskDueue = taskDueue;
        this.rejectPolicy = rejectPolicy;
    }

    public void execute(Runnable task) {
        int count = runningCount.get();
        //执行任务的线程数量少于coreSize
        if(count < coreSize){
            if(addWorker(task,true)){//开启新的核心线程
                return;
            }
        }
        if(taskDueue.offer(task)){//加入队列
            //此时，运行线程数量达到核心线程，并且可以进入任务队列
        }else{
            if(!addWorker(task,false)){//开启非核心线程
                //rejectPolicy.reject(task,this);

                //线程数量达到maxSize,
                if(!taskDueue.offer(task)){//再次尝试加入任务队列
                    //执行拒绝策略
                    rejectPolicy.reject(task,this);
                }
            }
        }
    }


    private boolean addWorker(Runnable newTask,boolean core){
        //自旋，创建线程失败后，再次尝试，直到达到限制数量
        for (;;) {
            int count = runningCount.get();
            int max = core ? coreSize : maxSize;
            if(count >= max){
                return false;
            }

            if(runningCount.compareAndSet(count,count+1)){
                String threadName = (core ? "core_" : "") + name + sequence.incrementAndGet();
                new Thread(()->{
                    System.out.println("thread name: " + Thread.currentThread().getName());
                    Runnable task = newTask;
                    while ( task != null || (task = getTask()) != null ){
                        try {
                            task.run();
                        }finally {
                            task = null;
                        }
                    }
                },threadName).start();
                break;
            }
        }
        return true;
    }

    private Runnable getTask(){
        try {
            return taskDueue.take();
        } catch (InterruptedException e) {
            //当前线程取任务失败-->线程退出
            runningCount.decrementAndGet();
            return null;
        }
    }




}
