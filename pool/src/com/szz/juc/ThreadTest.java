package com.szz.juc;

import java.util.concurrent.TimeUnit;

public class ThreadTest {
    public static void main(String[] args) {
        int threadCount = 3;
        for (int i = 0; i < threadCount; i++) {
            //new MyThread().run();
            new Thread(new MyThread()).start();
        }

        try {
            TimeUnit.SECONDS.sleep(3);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }finally {
            System.out.println(MySource.i);
        }

    }

}

class MySource{
    static Integer i = 0;
    static final Integer frequency = 100;
}

class MyThread implements Runnable{

    @Override
    public void run() {
        increase();
    }

    private void increase(){
        for (int i = 0; i < MySource.frequency; i++) {
            MySource.i ++;
        }
    }

}
