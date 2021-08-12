package com.szz.juc.pool;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

public class MyTheadPoolExecutorTest {
    public static void main(String[] args) {
        MyTheadPoolExecutor myTheadPoolExecutor = new MyTheadPoolExecutor("test",5,10, new ArrayBlockingQueue<Runnable>(15),new DiscardRejectPolicy());
        AtomicInteger num = new AtomicInteger(0);
        for (int i = 0; i < 30; i++) {
            myTheadPoolExecutor.execute(()->{
                try {
                    Thread.sleep(100);
                    System.out.println("running: " + System.currentTimeMillis() + ": "  + num.incrementAndGet());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            });
        }
    }
}
