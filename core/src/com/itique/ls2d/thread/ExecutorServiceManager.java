package com.itique.ls2d.thread;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class ExecutorServiceManager {

    private final ExecutorService executorService;

    public ExecutorServiceManager() {
        this.executorService = Executors.newSingleThreadExecutor();
    }

    public void addTask(Runnable r) {
        this.executorService.execute(r);
    }

}
