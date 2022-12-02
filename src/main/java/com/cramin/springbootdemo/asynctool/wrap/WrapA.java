package com.cramin.springbootdemo.asynctool.wrap;

import com.cramin.springbootdemo.asynctool.callback.CallbackA;
import com.cramin.springbootdemo.asynctool.work.WorkA;
import com.jd.platform.async.executor.Async;
import com.jd.platform.async.executor.timer.SystemClock;
import com.jd.platform.async.wrapper.WorkerWrapper;

import java.util.concurrent.ExecutionException;

public class WrapA {

    public static void main(String[] args) throws ExecutionException, InterruptedException {

        WorkerWrapper<String, String> wrapper = new WorkerWrapper.Builder<String, String>()
                .worker(new WorkA())
                .callback(new CallbackA())
                .param("123")
                .build();
        System.out.println("begin-" + SystemClock.now());
        Async.beginWork(1500, wrapper);
        System.out.println("end-" + SystemClock.now());
        Async.shutDown();
    }
}
