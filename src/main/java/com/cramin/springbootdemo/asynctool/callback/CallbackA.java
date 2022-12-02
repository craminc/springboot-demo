package com.cramin.springbootdemo.asynctool.callback;

import com.jd.platform.async.callback.ICallback;
import com.jd.platform.async.worker.WorkResult;

public class CallbackA implements ICallback<String, String> {

    @Override
    public void begin() {
        // callback when task started
        System.out.println("begin...");
    }

    @Override
    public void result(boolean success, String param, WorkResult<String> workResult) {
        System.out.println("param: " + param);
        // callback when task overed
        if (success) {
            System.out.println("success: " + workResult.getResult());
        } else {
            System.out.println("fail: " + workResult.getResult());
        }
    }
}
