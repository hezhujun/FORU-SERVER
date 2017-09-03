package com.wingsglory.foru.server.service;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by hezhujun on 2017/9/3.
 */
public class TaskPool {
    public static ExecutorService pool = Executors.newCachedThreadPool();

    public static void submitTask(Runnable task) {
        pool.submit(task);
    }

}
