package com.cramin.springbootdemo.asynctool.work;

import com.jd.platform.async.callback.IWorker;
import com.jd.platform.async.wrapper.WorkerWrapper;

import java.util.Map;
import java.util.concurrent.TimeUnit;

public class WorkA implements IWorker<String, String> {

    @Override
    public String action(String object, Map<String, WorkerWrapper> allWrappers) {
        System.out.println(object);
        // do something
        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        System.out.println("do action over");
        return "work A executed successfully";
    }

    @Override
    public String defaultValue() {
        // if action executed failed, return this default value
        return "work A executed unsuccessfully";
    }
}
