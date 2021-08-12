package com.szz.juc.MyThreadPool;

public interface Future<T> {
    T get();
}
