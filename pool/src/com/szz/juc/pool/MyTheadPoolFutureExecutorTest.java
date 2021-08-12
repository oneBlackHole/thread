package com.szz.juc.pool;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

public class MyTheadPoolFutureExecutorTest {
    public static void main(String[] args) {
        MyTheadPoolFutureExecutor executor = new MyTheadPoolFutureExecutor("futureExecutor",5,10,
                new ArrayBlockingQueue<>(15),new CallerRunRejectPolicy());
        AtomicInteger sequence = new AtomicInteger(0);
        List<TaskFuture<Object>> results = new ArrayList<>();
        for (int i = 0; i < 50; i++) {
            try {
                Thread.sleep(1000);
                TaskFuture<Object> result = executor.submit(()->{
                    sequence.incrementAndGet();
                    System.out.println("callable run sequece is " + sequence.get() );
                    return sequence;
                });
                results.add(result);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }


        }
        for (TaskFuture<Object> result : results) {
            System.out.println(result.get().toString());
        }
    }
}
